package model;

import java.util.List;

public record Invoice(String customerName, List<Performance> performances) {
}
