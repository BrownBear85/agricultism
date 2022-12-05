package com.brownbear85.agricultism;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public final class /**/AssetCreator {
    public static final String MOD_PATH = "C:\\Users\\Connor Wright\\Desktop\\Agricultism";

    public static class Templates {
        public static final String basicBlockstate = """
                {
                  "variants": {
                    "": {
                      "model": "agricultism:block/%s"
                    }
                  }
                }""";
        public static final String basicBlockModel = """
                {
                  "parent": "minecraft:block/cube_all",
                  "textures": {
                    "all": "agricultism:block/%s"
                  }
                }""";
        public static final String basicBlockItemModel = """
                {
                  "parent": "agricultism:block/%s"
                }
                """;
        public static final String basicItemModel = """
                {
                  "parent": "minecraft:item/generated",
                  "textures": {
                    "layer0": "agricultism:item/%s"
                  }
                }""";
        public static final String basicBlockLootTable = """
                {
                  "type": "minecraft:block",
                  "pools": [
                    {
                      "rolls": 1,
                      "entries": [
                        {
                          "type": "minecraft:item",
                          "name": "agricultism:%s"
                        }
                      ]
                    }
                  ]
                }
                            
                """;
    }
    /* util methods */

    public static void createJSON(String path, String name, String contents) {
        try {
            File file = new File(MOD_PATH + "\\src\\main\\resources\\" + path + "\\" + name + ".json");
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write(contents);
                writer.close();
                System.out.println("Created " + path + "\\" + name);
            } else {
                System.out.println("Skipped " + path + "\\" + name + " | file already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred whilst creating " + path + "\\" + name);
        }
    }

    public static String replace(String str, String target, String replace) {
        StringBuilder wordBuilder = new StringBuilder();
        StringBuilder correctWordBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            for (int ch = 0; ch < target.length(); ch++) {
                if (str.charAt(i + ch) == target.charAt(ch)) {
                    correctWordBuilder.append(str.charAt(i + ch));
                    if (ch + 1 == target.length()) {
                        wordBuilder.append(replace);
                        correctWordBuilder.delete(0, correctWordBuilder.length());
                        i += target.length() - 1;
                    }
                } else {
                    correctWordBuilder.append(str.charAt(i + ch));
                    wordBuilder.append(correctWordBuilder);
                    i += correctWordBuilder.length() - 1;
                    correctWordBuilder.delete(0, correctWordBuilder.length());
                    break;
                }
            }
        }
        return wordBuilder.toString();
    }

    public static String idToName(String id) {
        String[] arr = id.replace("_", " ").split("\\s");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
            if (i > 0) arr[i] = " " + arr[i];
            result.append(arr[i]);
        }
        return result.toString();
    }

    /* file creation methods */

    private static void createBasicBlockState(String name) {
        createJSON("assets\\agricultism\\blockstates", name, String.format(Templates.basicBlockstate, name));
    }

    private static void createBasicBlockModel(String name) {
        createJSON("assets\\agricultism\\models\\block", name, String.format(Templates.basicBlockModel, name));
    }

    private static void createBasicBlockitemModel(String name) {
        createJSON("assets\\agricultism\\models\\item", name, String.format(Templates.basicBlockItemModel, name));
    }

    private static void createBasicItemModel(String name) {
        createJSON("assets\\agricultism\\models\\item", name, String.format(Templates.basicItemModel, name));
    }

    private static void createBasicBlockLootTable(String name) {
        createJSON("data\\agricultism\\loot_tables\\blocks", name, String.format(Templates.basicBlockLootTable, name));
    }

    private static void addToLang(String key, String value) {
        try {
            File file = new File(MOD_PATH + "\\src\\main\\resources\\assets\\agricultism\\lang\\en_us.json");
            Scanner scanner = new Scanner(file);
            StringBuilder builder = new StringBuilder();
            String data;
            while (scanner.hasNextLine()) {
                data = scanner.nextLine();
                builder.append(data).append("\n");
            }

            String lang = builder.toString().split("}")[0];
            if (lang.contains(key)) {
                System.out.println("Key " + key + " already exists in lang, skipping it");
                return;
            }

            lang = lang.substring(0, lang.length() - 1) + String.format(",%n  \"%s\": \"%s\"%n}", key, value);
            FileWriter writer = new FileWriter(file);
            writer.write(lang);
            writer.close();

            System.out.println("Added " + key + " to lang");
        } catch (Exception e) {
            System.out.println("An error occurred whilst adding " + key + " to lang");
        }
    }

    private static void addToTag(String path, String tag, String item) {
        try {
            File file = new File(MOD_PATH + "\\src\\main\\resources\\data\\" + path + "\\" + tag + ".json");
            Scanner scanner = new Scanner(file);
            StringBuilder builder = new StringBuilder();
            String data;
            while (scanner.hasNextLine()) {
                data = scanner.nextLine();
                builder.append(data).append("\n");
            }

            String lang = builder.toString().split("]")[0];
            if (lang.contains(item)) {
                System.out.println("Item " + item + " is already in tag " + tag);
            } else {
                lang = lang.substring(0, lang.length() - 3) + String.format(",%n    \"%s\"%n  ]%n}", item);
                FileWriter writer = new FileWriter(file);
                writer.write(lang);
                writer.close();
                System.out.println("Added " + item + " to tag " + tag);
            }

        } catch (Exception e) {
            System.out.println("An error occurred whilst adding " + item + " to tag " + tag);
        }
    }

    /* block/item creation methods */

    public static void cloneRecipe(String recipe, String[][] strings, String name) {
        try {
            File file = new File("C:\\Users\\Connor Wright\\Desktop\\Files\\minecraft assets\\data\\minecraft\\recipes\\" + recipe + ".json");
            Scanner scanner = new Scanner(file);
            StringBuilder builder = new StringBuilder();
            String data;
            while (scanner.hasNextLine()) {
                data = scanner.nextLine();
                builder.append(data).append("\n");
            }

            String newRecipe = builder.toString();
            for (int i = 0; i < strings[0].length; i++) {
                newRecipe = replace(newRecipe, strings[0][i], strings[1][i]);
            }

            createJSON("data\\agricultism\\recipes\\", name, newRecipe);
        } catch (Exception e) {
            System.out.println("An error occurred while cloning recipe " + name);
        }
    }

    public static void createBasicBlock(String name) {
        createBasicBlockState(name);
        createBasicBlockModel(name);
        createBasicBlockLootTable(name);
        createBasicBlockitemModel(name);
        addToLang("block.agricultism." + name, idToName(name));
    }

    public static void createBasicItem(String name) {
        createBasicItemModel(name);
        addToLang("item.agricultism." + name, idToName(name));
    }

    public static void main(String[] args) {
        createBasicItem("oak_bark");
        createBasicItem("birch_bark");
        createBasicItem("spruce_bark");
        createBasicItem("dark_oak_bark");
        createBasicItem("jungle_bark");
        createBasicItem("acacia_bark");
        createBasicItem("mangrove_bark");
        createBasicItem("crimson_bark");
        createBasicItem("warped_bark");

    }
}