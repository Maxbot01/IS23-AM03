package it.polimi.ingsw.view;

public class CLIColorsTest {
    public static void main(String[] args) {
        System.out.println(CLIColors.BLACK + "Testo in nero" + CLIColors.RESET);
        System.out.println(CLIColors.WHITE + "Testo in bianco" + CLIColors.RESET);
        System.out.println(CLIColors.BLACK_BACKGROUND + "Testo con sfondo nero" + CLIColors.RESET);
        System.out.println(CLIColors.GREEN_BACKGROUND + "Testo con sfondo verde" + CLIColors.RESET);
        System.out.println(CLIColors.YELLOW_BACKGROUND + "Testo con sfondo giallo" + CLIColors.RESET);
        System.out.println(CLIColors.BLUE_BACKGROUND + "Testo con sfondo blu" + CLIColors.RESET);
        System.out.println(CLIColors.PURPLE_BACKGROUND + "Testo con sfondo viola" + CLIColors.RESET);
        System.out.println(CLIColors.CYAN_BACKGROUND + "Testo con sfondo ciano" + CLIColors.RESET);
        System.out.println(CLIColors.WHITE_BACKGROUND + "Testo con sfondo bianco" + CLIColors.RESET);
    }
}
