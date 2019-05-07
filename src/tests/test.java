package tests;


import Solution.LeagalSentnce;
import Solution.StoryTesterImpl;
import org.junit.Test;

import static Solution.StoryTesterImpl.storyToSentenceList;

public class test {
    public void nadavTest ()
    {
        String str = "hello world\nopp frikin sucks!";
        String[] array = str.split(" |\n");
        for (String word:array) {
            System.out.println(word);
        }
    }
    @Test
    public void IdanTest() {
        System.out.println("Hello, Starting Idan Test !");
        String story = "Given a classroom with a capacity of 75\n" +
                "When the number of students in the classroom is 60\n" +
                "Then the classroom is not-full\n" +
                "When the number of students in the classroom is 80\n" +
                "Then the classroom is full";
        for (int i=0;i<5;i++){
            String str = storyToSentenceList(story).get(i);
            LeagalSentnce temp = new LeagalSentnce(str);
            System.out.println("Hello, Starting Idan Test !");
        }

    }

}

