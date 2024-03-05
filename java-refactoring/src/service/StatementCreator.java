package service;

import com.google.gson.JsonObject;
import model.Invoice;
import model.Performance;
import model.Play;
import model.PlayType;

import java.util.List;

import static model.PlayType.comedy;


public class StatementCreator {

    public static String createStatement(Invoice invoice, JsonObject plays) {

        int totalAmount = 0;
        int volumeCredits = 0;

        String result = "Statement for " + invoice.customerName() + " \n";
        final List<Performance> performances = invoice.performances();

        for (Performance performance : performances) {
            int thisAmount = 0;
            int audience = performance.audienceCount();
            final JsonObject jsonPlay = plays.get(performance.playId())
                    .getAsJsonObject();
            Play play = new Play(performance.playId(),
                    jsonPlay.get("name").getAsString(),
                    PlayType.valueOf(jsonPlay.get("type").getAsString()));

            switch (play.type()) {
                case tragedy:
                    thisAmount = 40000;
                    if (audience > 30) {
                        thisAmount += 1000 * (audience - 30);
                    }
                    break;
                case comedy:
                    thisAmount = 30000;
                    if (audience > 20) {
                        thisAmount += 10000 + 500 * (audience - 20);
                    }
                    thisAmount += 300 * audience;
                    break;
                default:
                    return "error";
            }

            // add volume credits
            volumeCredits += Math.max(audience - 30, 0);

            // add extra credit for every ten comedy attendees
            if (comedy == play.type())
                volumeCredits += Math.floor(audience / 5);

            // print line for this order
            result += " " + play.name() + ": $" + (thisAmount / 100) + "(" + audience + " seats)\n";
            totalAmount += thisAmount;
        }

        result += "Amount owed is $" + (totalAmount / 100) + "\n";
        result += "You earned " + volumeCredits + " credits\n";
        return result;
    }
}
