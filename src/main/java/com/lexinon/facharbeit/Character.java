package com.lexinon.facharbeit;

import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class Character {

    private static final Map<String, Vector2f> map = new HashMap<>();

    static {
        character(" ", new Vector2f( 0, 0));
        character("!", new Vector2f( 1, 0));
        character("\"",new Vector2f( 2, 0));
        character("#", new Vector2f( 3, 0));
        character("$", new Vector2f( 4, 0));
        character("%", new Vector2f( 5, 0));
        character("&", new Vector2f( 6, 0));
        character("'", new Vector2f( 7, 0));
        character("(", new Vector2f( 8, 0));
        character(")", new Vector2f( 9, 0));
        character("*", new Vector2f(10, 0));
        character("+", new Vector2f(11, 0));
        character(",", new Vector2f(12, 0));
        character("-", new Vector2f(13, 0));
        character(".", new Vector2f(14, 0));
        character("/", new Vector2f(15, 0));

        character("0", new Vector2f( 0, 1));
        character("1", new Vector2f( 1, 1));
        character("2", new Vector2f( 2, 1));
        character("3", new Vector2f( 3, 1));
        character("4", new Vector2f( 4, 1));
        character("5", new Vector2f( 5, 1));
        character("6", new Vector2f( 6, 1));
        character("7", new Vector2f( 7, 1));
        character("8", new Vector2f( 8, 1));
        character("9", new Vector2f( 9, 1));
        character(":", new Vector2f(10, 1));
        character(";", new Vector2f(11, 1));
        character("<", new Vector2f(12, 1));
        character("=", new Vector2f(13, 1));
        character(">", new Vector2f(14, 1));
        character("?", new Vector2f(15, 1));

        character("@", new Vector2f( 0, 2));
        character("A", new Vector2f( 1, 2));
        character("B", new Vector2f( 2, 2));
        character("C", new Vector2f( 3, 2));
        character("D", new Vector2f( 4, 2));
        character("E", new Vector2f( 5, 2));
        character("F", new Vector2f( 6, 2));
        character("G", new Vector2f( 7, 2));
        character("H", new Vector2f( 8, 2));
        character("I", new Vector2f( 9, 2));
        character("J", new Vector2f(10, 2));
        character("K", new Vector2f(11, 2));
        character("L", new Vector2f(12, 2));
        character("M", new Vector2f(13, 2));
        character("N", new Vector2f(14, 2));
        character("O", new Vector2f(15, 2));

        character("P", new Vector2f( 0, 3));
        character("Q", new Vector2f( 1, 3));
        character("R", new Vector2f( 2, 3));
        character("S", new Vector2f( 3, 3));
        character("T", new Vector2f( 4, 3));
        character("U", new Vector2f( 5, 3));
        character("V", new Vector2f( 6, 3));
        character("W", new Vector2f( 7, 3));
        character("X", new Vector2f( 8, 3));
        character("Y", new Vector2f( 9, 3));
        character("Z", new Vector2f(10, 3));
        character("[", new Vector2f(11, 3));
        character("\\",new Vector2f(12, 3));
        character("]", new Vector2f(13, 3));
        character("^", new Vector2f(14, 3));
        character("_", new Vector2f(15, 3));

        character("`", new Vector2f( 0, 4));
        character("a", new Vector2f( 1, 4));
        character("b", new Vector2f( 2, 4));
        character("c", new Vector2f( 3, 4));
        character("d", new Vector2f( 4, 4));
        character("e", new Vector2f( 5, 4));
        character("f", new Vector2f( 6, 4));
        character("g", new Vector2f( 7, 4));
        character("h", new Vector2f( 8, 4));
        character("i", new Vector2f( 9, 4));
        character("j", new Vector2f(10, 4));
        character("k", new Vector2f(11, 4));
        character("l", new Vector2f(12, 4));
        character("m", new Vector2f(13, 4));
        character("n", new Vector2f(14, 4));
        character("o", new Vector2f(15, 4));

        character("p", new Vector2f( 0, 5));
        character("q", new Vector2f( 1, 5));
        character("r", new Vector2f( 2, 5));
        character("s", new Vector2f( 3, 5));
        character("t", new Vector2f( 4, 5));
        character("u", new Vector2f( 5, 5));
        character("v", new Vector2f( 6, 5));
        character("w", new Vector2f( 7, 5));
        character("x", new Vector2f( 8, 5));
        character("y", new Vector2f( 9, 5));
        character("z", new Vector2f(10, 5));
        character("{", new Vector2f(11, 5));
        character("|", new Vector2f(12, 5));
        character("}", new Vector2f(13, 5));
        character("~", new Vector2f(14, 5));

        character("Ä", new Vector2f(15, 5));
        character("Ö", new Vector2f(15, 5));
        character("Ü", new Vector2f(15, 5));
        character("ä", new Vector2f(15, 5));
        character("ö", new Vector2f(15, 5));
        character("ü", new Vector2f(15, 5));
        character("ß", new Vector2f(15, 5));
    }

    private Character() {}

    private static void character(String character, Vector2f texture) {
        map.put(character, texture);
    }

    public static Vector2f getTexture(char character) {
        return map.getOrDefault(String.valueOf(character), new Vector2f());
    }

}
