package org.uex_back.validation;

public final class CpfValidator {

    private CpfValidator() {}

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        // remove máscara
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;

        // rejeita sequências tipo 00000000000, 11111111111, etc.
        if (cpf.chars().distinct().count() == 1) return false;

        try {
            int d1 = calculateDigit(cpf, 10);
            int d2 = calculateDigit(cpf, 11);

            return cpf.charAt(9) == (char) (d1 + '0') &&
                    cpf.charAt(10) == (char) (d2 + '0');
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int calculateDigit(String cpf, int weightStart) {
        int sum = 0;
        for (int i = 0; i < weightStart - 1; i++) {
            int num = cpf.charAt(i) - '0';
            sum += num * (weightStart - i);
        }
        int resto = sum % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}