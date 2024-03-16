package com.app.notelock;

import java.util.Random;

public class PasswordGenerator {
    public static String generate(int length, int min_numbers, int min_symbols, boolean capital, boolean small, boolean numbers, boolean symbol){
        String password = "";
        int current_length = 0;
        Random random = new Random();
        if(capital){
            password = password + (char)(random.nextInt(26) + 'A');
            current_length++;
        }

        if(small){
            password = password + (char)(random.nextInt(26) + 'a');
            current_length++;
        }

        if(numbers){
            password = password + random.nextInt(10);
            current_length++;
        }

        if(symbol){
            password = password + specialChar(random.nextInt(10));
            current_length++;
        }
        if(numbers){
            for(int i = 0 ; i < min_numbers - 1; i++){
                password = password + random.nextInt(10);
                current_length++;
            }
        }

        if(symbol){
            for(int i = 0 ; i < min_symbols - 1; i++){
                password = password + specialChar(random.nextInt(10));
                current_length++;
            }
        }
        for(int i = 0 ; current_length < length ; i++){
            password = password + (char)(random.nextInt(26) + 'a');
            current_length++;
        }
        return password;
    }
    private static char specialChar(int index){
        char ch;
        if(index == 0){
            ch = '!';
        }
        else if(index == 1){
            ch = '@';
        }
        else if(index == 2){
            ch = '#';
        }
        else if(index == 3){
            ch = '$';
        }
        else if(index == 4){
            ch = '%';
        }
        else if(index == 5){
            ch = '^';
        }
        else if(index == 6){
            ch = '&';
        }
        else if(index == 7){
            ch = '*';
        }
        else if(index == 8){
            ch = '(';
        }
        else {
            ch = ')';
        }

        return ch;
    }
}