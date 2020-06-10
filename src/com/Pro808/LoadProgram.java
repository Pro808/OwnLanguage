package com.Pro808;

import java.io.*;
import java.nio.charset.StandardCharsets;


class LoadProgram {
    private String Path;
    private StringBuilder data = new StringBuilder("");

    LoadProgram(String Path){
        this.Path = Path;
        readFile();
    }

    String getProgram(){
        return this.data.toString();
    }

    private void readFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Path, StandardCharsets.UTF_8));
            reader.lines().iterator().forEachRemaining((line)->{
                data.append(line);
            });
        } catch (IOException e) {
            System.out.println("Не удалось загрузить программу, ошибка: " + e.getMessage());
            createProgramFile();
        }
    }

    private void createProgramFile(){
        String[] arrPath = Path.split("/");
        String fileName = arrPath[arrPath.length-1];
        File newFile = new File("./" + fileName);
        boolean created = false;
        if(!newFile.exists()){
            try {
                created = newFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Не удалось создать файл с именем (" + fileName + "). Ошибка: " + e.getMessage());
            }
            if(created){
                System.out.println("Новый файл программы создан! Имя: " + fileName);
            }
        }
    }
}
