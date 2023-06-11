package nl.inholland.bankapi.models.dto;

public record RegisterDTO(String firstName, String lastName, String email, String phone, String password) {
}
