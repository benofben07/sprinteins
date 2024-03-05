import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Invoice;
import model.Performance;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final JsonParser parser = new JsonParser();

    public static JsonObject parsePlays() throws Exception {
        final String playsFile = new String(Files.readAllBytes(new File("plays.json").toPath()));
        final JsonObject plays = parser.parse(playsFile).getAsJsonObject();
        return plays;
    }

    public static Invoice parseInvoices() throws Exception {
        final String invoicesFile = new String(Files.readAllBytes(new File("invoices.json").toPath()));
        final JsonArray jsonInvoices = parser.parse(invoicesFile).getAsJsonArray();
        final String jsonCustomerName = jsonInvoices.get(0).getAsJsonObject().get("customer").getAsString();
        final List<Performance> performances = new ArrayList<>();
        final JsonArray jsonPerformances = jsonInvoices.get(0).getAsJsonObject().get("performances").getAsJsonArray();
        for (int index = 0; index < jsonPerformances.size(); ++index) {
            String playID = jsonPerformances.get(index).getAsJsonObject().get("playID").getAsString();
            int audience = jsonPerformances.get(index).getAsJsonObject().get("audience").getAsInt();

            performances.add(new Performance(playID, audience));
        }

        return new Invoice(jsonCustomerName, performances);
    }
}
