package Solution;

import java.util.ArrayList;
import java.util.LinkedList;

public class LeagalSentnce {
    String input;
    String type;
    ArrayList<ArrayList<String>> parametrs;
    String comperable;

    public LeagalSentnce(String str) {
        this.input = str;
        this.type = str.split(" ", 2)[0];
        parametrs = new ArrayList<ArrayList<String>>();

        String[] centences = str.split("or");
        comperable = centences[0].split(" ", 2)[1];
        for (String subStr : centences) {

            String[] array = subStr.split("and");
            ArrayList<String> comperableTemp = new ArrayList<String>();
            for (String S : array) {
                String[] temp = S.split(" ");
                comperableTemp.add(temp[temp.length - 1]);
            }
            parametrs.add(comperableTemp);

        }
        for (String param : parametrs.get(0)) {
            comperable = comperable.replace(param, "&");
        }
    }
    public LeagalSentnce(LeagalSentnce cent)
    {
        this.input = cent.input;
        this.type = cent.type;
        this.parametrs = new ArrayList<>();
        for(ArrayList<String> param:cent.parametrs)
        {
            this.parametrs.add(new ArrayList<>(param));
        }
        this.comperable = cent.comperable;
    }

    public String getInput() {
        return input;
    }

    public String getType() {
        return type;
    }

    public ArrayList<ArrayList<String>> getParametrs() {
        //for(Person p : oldList) {
        //    newList.add(p.clone());
        //}
        ArrayList<ArrayList<String>> toRet = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> a : parametrs) {
            toRet.add(new ArrayList<String>(a));
        }
        return toRet;
    }

    public String getComperable() {
        return comperable;
    }
}