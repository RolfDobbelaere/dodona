import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
public class MatchAnalysis implements IMatchAnalysis {
    private String teamName;
    private double ballPossession;
    private Player[] players;
    private IGraph passDistribution;
    public MatchAnalysis(String teamName, double ballPossession, Player[] players, IGraph passDistribution) {
        this.teamName = teamName;
        this.ballPossession = ballPossession;
        this.players = players;
        this.passDistribution = passDistribution;
    }
    public String getTeamName() {
        return teamName;
    }
    public double getBallPossession() {
        return ballPossession;
    }
    public Player[] getPlayers() {
        return players;
    }
    public IGraph getPassDistribution() {
        return passDistribution;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void setBallPossession(double ballPossession) {
        this.ballPossession = ballPossession;
    }
    public static MatchAnalysis readTeamInfo(File f) {
        ArrayList<String[]> lines = new ArrayList<>(); // CSV bestand volledig gesplits per lijn
        ArrayList<String[]> generalInfo = new ArrayList<>(); // CSV bestand lijn 1-4 (general info)
        ArrayList<String[]> playerList = new ArrayList<>(); // CSV bestand lijn 6 tot 6 + aantal spelers (lijst met spelers)
        ArrayList<String[]> passStats = new ArrayList<>(); // CSV bestand rest van lijnen voor passtatistieken (lijst met edges)
        int playerAmount = 0;
        try {
            //inlezen van het CSV-bestand en opdelen in substukken
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                lines.add(s.nextLine().split(";"));
            }
            for (int i = 0; i < 4; i++)
                generalInfo.add(lines.get(i));
            playerAmount = Integer.parseInt(generalInfo.get(3)[1]);
            for (int i = 6; i < 6 + playerAmount; i++) // vanaf lijn 6 tot einde players
                playerList.add(lines.get(i));
            for (int i = 8 + playerAmount; i < 8 + playerAmount * 2; i++) // vanaf einde players+1 tot einde passtats
                passStats.add(lines.get(i));
        } catch (IOException e) {
            System.out.println("Het bestand '" + f.getName() + "' bestaat niet!");
            return null;
        }
        //Inlezen naam, balbezit, lijst van spelers
        String naam = generalInfo.get(1)[1];
        double balbezit = Double.parseDouble(generalInfo.get(2)[1]);
        Player[] players = new Player[playerAmount];
        for (int i = 0; i < playerAmount; i++)
            players[i] = new Player(playerList.get(i)[0], Integer.parseInt(playerList.get(i)[1]));
        //Toevoegen van passen & spelers aan het graph-object
        HashMap<INode, ArrayList<IEdge>> proximityLists = new HashMap<>();
        IGraph graph = new Graph(proximityLists);
        for (Player p : players) {
            try {
                graph.addNode(p);
            } catch (GraphAdditionException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        for (int i = 0; i < playerAmount; i++)
            for (int j = 1; j <= playerAmount; j++)
                if (!passStats.get(i)[j].equals("0")) {
                    try {
                        graph.addEdge(new Pass(players[i], players[j - 1], Integer.parseInt(passStats.get(i)[j])));
                    } catch (GraphAdditionException e) {
                        System.out.println(e.getMessage());
                        return null;

                    }
                }
        return new MatchAnalysis(naam, balbezit, players, graph);

    }
    public double calculateGroupIntensity(INode[] nodes) {
        double IT = 0.0;
        if (nodes == null || nodes.length == 0)
            return 0.0;
        for (INode node : nodes)
            try {
                int strength = (passDistribution.calculateOutStrenghtOfNode(node)
                        + passDistribution.calculateInStrenghtOfNode(node)) / 2;
                IT += strength;
            } catch (GraphQueryException e) {
                System.out.print(e.getMessage() + "Dus is deze node genegeerd.");
            }
        IT = IT / ballPossession;
        return IT;
    }
    @Override
    public double calculateTeamIntensity() {
        return calculateGroupIntensity(players);
    }
    @Override
    public String toString() {
        String s = "";
        Arrays.sort(players);
        s += teamName + "\n";
        s += "--------------\n";
        s += "Balbezit: " + ballPossession + "%\n";
        s += "Intensiteit: " + calculateTeamIntensity() + "\n";
        s += "Opgestelde Players:\n";
        for (Player player : players) {
            s += player.toString() + "\n";
        }
        return s;
    }
}

