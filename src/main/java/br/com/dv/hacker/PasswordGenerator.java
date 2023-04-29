package br.com.dv.hacker;

public class PasswordGenerator {

    public String generateNextPassword(String currentPassword) {
        StringBuilder newPassword = new StringBuilder(currentPassword);
        int index = newPassword.length() - 1;

        while (index >= 0) {
            char currentChar = newPassword.charAt(index);
            if (currentChar == 'z') {
                newPassword.setCharAt(index, '0');
                break;
            } else if (currentChar == '9') {
                newPassword.setCharAt(index, 'a');
                index--;
            } else {
                newPassword.setCharAt(index, (char) (currentChar + 1));
                break;
            }
        }

        if (index < 0) {
            newPassword.insert(0, 'a');
        }

        return newPassword.toString();
    }

}
