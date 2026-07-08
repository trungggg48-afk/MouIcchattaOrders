package me.MouIcchatta.order.model;

import java.util.List;

public class OrderAction {
    private final String id;
    private final String display;
    private final String price;
    private final String link;
    private final List<String> consoleCommands;

    public OrderAction(String id, String display, String price, String link, List<String> consoleCommands) {
        this.id = id;
        this.display = display;
        this.price = price;
        this.link = link;
        this.consoleCommands = consoleCommands;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public String getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    public List<String> getConsoleCommands() {
        return consoleCommands;
    }
}
