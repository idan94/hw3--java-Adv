package Solution;

import Solution.LeagalSentnce.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Story {

    private String input;
    private ArrayList<LeagalSentnce> Sentences;

    public Story (String str)
    {
        input = str;
        Sentences = new ArrayList<>();
        String[] array = str.split("\n");
        for(String cent:array)
        {
            Sentences.add(new LeagalSentnce(cent));
        }
    }

    public String getInput() {
        return input;
    }

    public ArrayList<LeagalSentnce> getSentence() {
        ArrayList<LeagalSentnce> toRet = new ArrayList<>();
        for (LeagalSentnce cent:Sentences)
        {
            toRet.add(new LeagalSentnce(cent));
        }
        return toRet;
    }

}
