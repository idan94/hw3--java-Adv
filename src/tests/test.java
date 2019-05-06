package tests;

import org.junit.Test;

public class test {
    public static void main (String []args)
    {
        String str = "hello world\nopp frikin sucks!";
        String[] array = str.split(" |\n");
        for (String word:array) {
            System.out.println(word);
        }
    }
}
