package Solution;

import Solution.LegalSentnce.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Story {

    private String input;
    private ArrayList<LegalSentnce> Sentences;

    public Story (String str)
    {
        input = str;
        Sentences = new ArrayList<>();
        String[] array = str.split("\n");
        for(String sentence:array)
        {
            Sentences.add(new LegalSentnce(sentence));
        }
    }

    public String getInput() {
        return input;
    }

    public ArrayList<LegalSentnce> getSentence() {
        ArrayList<LegalSentnce> toRet = new ArrayList<>();
        for (LegalSentnce sentence:Sentences)
        {
            toRet.add(new LegalSentnce(sentence));
        }
        return toRet;
    }

}
