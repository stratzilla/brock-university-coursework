package Assign_4;

import BasicIO.*;

//the formatting is way off despite using the appropriate field widths
//even opening lab files at home yields wonky formatting when on
//university computers they work fine. I had to do this assignment at home
//because I am sick, so I couldn't get a properly formatted PDF from Brock
//I hope it won't result in too many marks lost, the formatting is only
//wrong because I don't have a proper PDF creator as Brock does

public class Assign4 {
    
    //global objects/variables
    private final ASCIIDataFile datafile; //text file
    private ASCIIPrompter prompt; //prompter
    private ASCIIDisplayer display; //displayer
    private final ReportPrinter report; //final report
    //declared global because multiple methods use them
    //I didn't want to keep passing parameters, so this works just as well
    String winner; //winner(s) who raised most
    String name; //person name
    int bars; //chocolate bars
    int mugs; //mugs
    int hats; //hats
    int abac; //abaci
    double total; //sum of profit
    //prices of each item
    double barprice;
    double hatprice;
    double mugprice;
    double abacprice;
    //arrays for winners
    String [] winNames = new String[5];
    double [] winTotals = new double[5];
        
    //constructor class
    public Assign4 () {
        datafile = new ASCIIDataFile();
        report = new ReportPrinter();
        //creates the actual report with columns, etc
        createReport();
        barprice = getPrices("Bar");
        hatprice = getPrices("Hat");
        mugprice = getPrices("Mug");
        abacprice = getPrices("Abac");
        //tally up the results, and put in the form
        tallyTotals();
    }
    
    //create the basis of form aesthetics
    private void createReport() {
        //split the title into two variable initializations
        //because otherwise it would extend beyond the page
        String title = "Broccoli University";
        String subtitle = "Saving the World With Bland Chocolate";
        report.setTitle(title,subtitle);
        report.addField("name","Name",25);
        report.addField("bars","Bars",4);
        report.addField("mugs","Mugs",4);
        report.addField("hats","Hats",4);
        report.addField("abac","Abaci",5);
        report.addField("total","Total",12);
    }
    
    //prompt user for price of each item
    private double getPrices(String type) {
        //create prompter
        prompt = new ASCIIPrompter();
        //initialize price var
        double price;
        //label prompter
        prompt.setLabel(type);
        price = prompt.readDouble();
        return price;
    }
    
    //all math, etc is in here; determines totals
    private void tallyTotals() {
        int totalbars = 0; //total bars
        int totalhats = 0; //total hats
        int totalmugs = 0; //total mugs
        int totalabac = 0; //total abaci
        double totaltotal = 0.00; //total totals
        //below two strings used to append $ to total
        String stringTotal;
        String stringTotalTotal;
        //variable used for array
        int q = 0;
        //continue until break condition
        for (;;) {
            name = datafile.readString();
            //if last line, stop loop
            if (datafile.isEOF()) {
                break;
            }
            winNames[q] = name;
            //read values in text file
            bars = datafile.readInt();
            mugs = datafile.readInt();
            hats = datafile.readInt();
            abac = datafile.readInt();
            //sum up the totals of each item
            totalbars = totalbars + bars;
            totalmugs = totalmugs + mugs;
            totalhats = totalhats + hats;
            totalabac = totalabac + abac;
            //calculate total based on price
            total = (bars*barprice)+(mugs*mugprice)+(hats*hatprice)+(abac*abacprice);
            //sum up the total of totals
            totaltotal = totaltotal + total;
            winTotals[q] = total;
            //write to report
            stringTotal = "$"+total;
            writeDetail(name, bars, mugs, hats, abac, stringTotal);
            //increment for next array element
            q++;
        }
        stringTotalTotal = "$"+totaltotal;
        //append line at bottom for winners + summaries of items
        writeSummary(totalbars, totalmugs, totalhats, totalabac, stringTotalTotal);
        //close appropriate objects
        datafile.close();
        report.close();
    }
    
    //create aesthetics of form
    private void writeDetail(String n, int b, int m, int h, int a, String st) {
        report.writeString("name",n);
        report.writeInt("bars",b);
        report.writeInt("mugs",m);
        report.writeInt("hats",h);
        report.writeInt("abac",a);
        report.writeString("total",st);
    }
    
    //create summary at bottom of form
    private void writeSummary(int tb, int tm, int th, int ta, String tt) {
        String stringWinner;
        report.writeString("name","Totals:");
        report.writeInt("bars",tb);
        report.writeInt("mugs",tm);
        report.writeInt("hats",th);
        report.writeInt("abac",ta);
        report.writeString("total",tt);
        //determine winner
        winner = determineWinner();
        stringWinner = "Lets hear it for "+winner;
        report.writeLine(stringWinner);
    }
    
    //determines the winner/the one who raised the most
    private String determineWinner() {
        //this is probably wayyy too much work for what was asked
        //but I couldn't figure out a simple way to do it
        int j;
        double placeholder = winTotals[0];
        String whoWon = winNames[0];
        for (j = 1; j < 5; j++){
            //determine highest profit per person, even if a tie
            if (winTotals[j] > placeholder) {
                //replace previous "winner" with new winner
                whoWon = winNames[j];
                placeholder = winTotals[j];
            } else if (placeholder == winTotals[j]) {
                //if equal to previous "winner", concatenate both into one
                whoWon = whoWon + " and " + winNames[j];
            }
        }
        //return winner or winners
        return whoWon;
    }

    public static void main(String[] args) {
        Assign4 a = new Assign4();
    }
}
