//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/*
public class Main {
    public static void main(String[] args) {
        Checkers newGame = new Checkers();
    }
}
*/

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CheckersGame newGame = new CheckersGame();
        newGame.startGame();
    }
}

class CheckersGame {
    public static long blackPieces = 0b0101010110101010010101010000000000000000000000000000000000000000L;
    public static long redPieces = 0b0000000000000000000000000000000000000000101010100101010110101010L;
    private long blackKings = 0b0000000000000000000000000000000000000000000000000000000000000000L;
    private long redKings = 0b0000000000000000000000000000000000000000000000000000000000000000L;
    private long emptyGame = 0b0000000000000000000000000000000000000000000000000000000000000000L;
    public static String colorTurn = "Black";

    public void startGame() {
        System.out.println("To calculate position, count manually, or use formula \"position = row x 8 + column\"");
        while (redPieces != emptyGame || blackPieces != emptyGame || redKings != emptyGame || blackKings != emptyGame) {

            printOut();

            Scanner scan = new Scanner(System.in);

            System.out.println(colorTurn + ", what piece would you like to move?");

            int from = scan.nextInt();

            System.out.println("Where would you like to move this piece?");

            int to = scan.nextInt();

            if (!makeMove(from, to)) {
                System.out.println("Move illegal!");
                continue;
            }

            switchTurns();

        }
    }

    private void switchTurns() {
        if (colorTurn.equalsIgnoreCase("Black")) {
            colorTurn = "Red";
        } else if (colorTurn.equalsIgnoreCase("Red")) {
            colorTurn = "Black";
        }
    }

    private boolean makeMove(int from, int to) {
        int absolute = Math.abs(from - to);

        long fromBit = 1L << 64 - from;
        long toBit = 1L << 64 - to;
        long halfToBit = 1L << 64 - (from - ((from - to) / 2));

        switch (colorTurn)
        {
            case "Black":
                if(!checkBitNull(blackPieces, from)) //REGULAR MOVE
                {
                    if(from - to > 0) //CANT MOVE UP MAP IF NOT KING
                    {
                        return false;
                    }
                    if (absolute == 14 || absolute == 18) //ATTEMPTING JUMP
                    {
                        if (!(checkBitNull(redPieces, to) && (redPieces & halfToBit) != 0)) //Check if jump is legal
                        {
                            return false;
                        }

                        if(to > 56) //Check if king promotion
                        {
                            blackPieces ^= fromBit; //Delete from old position
                            blackKings |= toBit; //Promote to king
                            redPieces ^= halfToBit; //Delete jumped piece
                        }
                        else //Regular jump
                        {
                            blackPieces ^= fromBit; //Delete from old position
                            blackPieces |= toBit; //Place at new position
                            redPieces ^= halfToBit; //Delete jumped piece
                        }
                        return true;
                    }
                    else if(absolute == 7 || absolute == 9) //IS REGULAR MOVE DIAGONAL?
                    {
                        if (!checkBitNull(redPieces, to)) //Check if regular move is legal
                        {
                            return false;
                        }

                        if(to > 56) //CHECK FOR KING PROMOTION
                        {
                            blackPieces ^= fromBit; //Delete from old position
                            blackKings |= toBit; //Promote to king
                        }
                        else //Continue regular move
                        {
                            blackPieces ^= fromBit; //Delete from old position
                            blackPieces |= toBit; //Place at new position
                        }
                        return true;
                    }
                    else //IF NO CRITERIA MET, MOVE IS ILLEGAL
                    {
                        return false;
                    }
                }
                else if (!checkBitNull(blackKings, from)) //KINGS MOVE
                {
                    if (absolute == 14 || absolute == 18) //ATTEMPTING JUMP
                    {
                        if (!(checkBitNull(redPieces, to) && (redPieces & halfToBit) != 0)) //Check if jump is legal
                        {
                            return false;
                        }

                        blackKings ^= fromBit; // Delete from old position
                        blackKings |= toBit; // Place at new position
                        redPieces ^= halfToBit; // Delete from old position

                        return true;
                    }
                    else if(absolute == 7 || absolute == 9) //IS REGULAR MOVE DIAGONAL?
                    {
                        if (!checkBitNull(redPieces, to)) //Check if regular move is legal
                        {
                            return false;
                        }

                        blackKings ^= fromBit; // Delete from old position
                        blackKings |= toBit; // Place at new position

                        return true;
                    }
                    else //IF NO CRITERIA MET, MOVE IS ILLEGAL
                    {
                        return false;
                    }
                }
                return false;
            case "Red":
                if (!checkBitNull(redPieces, from)) //Check if from is null
                {
                    if(from - to < 0) //CANT MOVE DOWN MAP IF NOT KING
                    {
                        return false;
                    }
                    if (absolute == 14 || absolute == 18) //Attempted jump
                    {
                        if (!(checkBitNull(blackPieces, to) && (blackPieces & halfToBit) != 0)) //Check if jump is legal
                        {
                            return false;
                        }

                        if(to < 9)
                        {
                            redPieces ^= fromBit; // Delete from old position
                            redKings |= toBit; // Place at new position
                            blackPieces ^= halfToBit; // Delete from old position
                        }
                        else
                        {
                            redPieces ^= fromBit; // Delete from old position
                            redPieces |= toBit; // Place at new position
                            blackPieces ^= halfToBit; // Delete from old position
                        }

                        return true;
                    }
                    else if(absolute == 7 || absolute == 9) //Regular move
                    {
                        if (!checkBitNull(redPieces, to))
                        {
                            return false;
                        }

                        if(to < 9)
                        {
                            redPieces ^= fromBit; // Delete from old position
                            redKings |= toBit; // Place at new position
                        }
                        else
                        {
                            redPieces ^= fromBit; // Delete from old position
                            redPieces |= toBit; // Place at new position
                        }

                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else if (!checkBitNull(redKings, from)) //KINGS MOVE
                {
                    if (absolute == 14 || absolute == 18) //Attempted jump
                    {
                        if (!(checkBitNull(blackPieces, to) && (blackPieces & halfToBit) != 0)) //Check if jump is legal
                        {
                            return false;
                        }

                            redKings ^= fromBit; // Delete from old position
                            redKings |= toBit; // Place at new position
                            blackPieces ^= halfToBit; // Delete from old position

                        return true;
                    }
                    else if(absolute == 7 || absolute == 9) //Regular move
                    {
                        if (!checkBitNull(redKings, to))
                        {
                            return false;
                        }

                        redKings ^= fromBit; // Delete from old position
                        redKings |= toBit; // Place at new position

                        return true;
                    }
                    else {
                        return false;
                    }
                }
                return false;
        }

        return true;
    }

    private boolean checkBitNull(long colorType, int position)
    {
        return (colorType & 1L << 64 - position) == 0;
    }

    private void printOut() {
        String[] binToBoard = new String[64];
        int counter = 0;
        for (int i = 63; i >= 0; i--) //Starting backwards since binary
        {
            if ((redPieces & (1L << i)) != 0)
            {
                binToBoard[counter] = "r ";
            } else {
                binToBoard[counter] = ". ";
            }
            counter++;
        }

        int counter2 = 0;
        for (int i = 63; i >= 0; i--) //Starting backwards since binary
        {
            if ((blackPieces & (1L << i)) != 0)
            {
                binToBoard[counter2] = "b ";
            } else if (!binToBoard[counter2].equals("r ")) {
                binToBoard[counter2] = ". ";
            }
            counter2++;
        }

        int counter3 = 0;
        for (int i = 63; i >= 0; i--) //Starting backwards since binary
        {
            if ((redKings & (1L << i)) != 0)
            {
                binToBoard[counter3] = "R ";
            }
            counter3++;
        }

        int counter4 = 0;
        for (int i = 63; i >= 0; i--) //Starting backwards since binary
        {
            if ((blackKings & (1L << i)) != 0)
            {
                binToBoard[counter4] = "B ";
            }
            counter4++;
        }


        String printout = "";

        for (int i = 1; i < 65; i++)
        {
            if (i == 1)
            {
                printout += "01" + " ";
            }
            printout += binToBoard[i - 1];

            if (i % 8 == 0)
            {
                printout += "\n";
                if (i < 10)
                {
                    printout += "0" + (i + 1) + " ";
                } else if (i < 64)
                {
                    printout += (i + 1) + " ";
                }
            }
        }
        System.out.println(printout);
    }
}
