package ru.ifmo.rain.dolgikh.walk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Walk {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        ArrayList<String> stringList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(inputFileName))) {
            String curStr;
            while ((curStr = in.readLine()) != null) {
                stringList.add(curStr);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file doesn't exist");
        } catch (IOException e) {
            System.out.println("Can't read input file");
        }


        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String curFileName : stringList) {
                byte[] bytes = null;
                try {
                    Path curFile = Paths.get(curFileName);
                    bytes = Files.readAllBytes(curFile);
                } catch (IOException e) {
                    System.out.println("File doesn't exist: " + curFileName);
                } catch (InvalidPathException e) {
                    System.out.println("Invalid file name:" + curFileName);
                }
                try {
                    bufferedWriter.write(String.format("%08x", calculateHash(bytes)) + " " + curFileName);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    System.out.println("Can't write");
                }
            }
        } catch (IOException e) {
            System.out.println("Can't open output file");
        }
    }


    private static int calculateHash(byte[] bytes) {
        if (bytes == null) return 0;
        int hash = 0x811c9dc5;
        for (byte b : bytes) {
            hash = (hash * 0x01000193) ^ (b & 0xff);
        }
        return hash;
    }

}
