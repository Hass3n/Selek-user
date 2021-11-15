package minya.salek.salekapp.Apiplaces.Customspinner.inventory;


import java.util.ArrayList;
import java.util.List;

import minya.salek.salekapp.R;

public class Data {

    public static List<Fruit> getFruitList() {
        List<Fruit> fruitList = new ArrayList<>();

        Fruit Avocado = new Fruit();
        Avocado.setName("Hospital");
        Avocado.setImage( R.drawable.logo);
        fruitList.add(Avocado);

        Fruit Banana = new Fruit();
        Banana.setName("Atm");
        Banana.setImage(R.drawable.logo);
        fruitList.add(Banana);

        Fruit Coconut = new Fruit();
        Coconut.setName("School");
        Coconut.setImage(R.drawable.logo);
        fruitList.add(Coconut);

        Fruit Guava = new Fruit();
        Guava.setName("Restuarant");
        Guava.setImage(R.drawable.logo);
        fruitList.add(Guava);


/*----------------------------------------------*/
        Fruit Guava2 = new Fruit();
        Guava2.setName("accounting");
        Guava2.setImage(R.drawable.logo);
        fruitList.add(Guava2);


        Fruit Guava3 = new Fruit();
        Guava3.setName("bakery");
        Guava3.setImage(R.drawable.logo);
        fruitList.add(Guava3);




        Fruit Guava4 = new Fruit();
        Guava4.setName("beauty_salon");
        Guava4.setImage(R.drawable.logo);
        fruitList.add(Guava4);


        Fruit Guava5 = new Fruit();
        Guava5.setName("bus_station");
        Guava5.setImage(R.drawable.logo);
        fruitList.add(Guava5);

        Fruit Guava6 = new Fruit();
        Guava6.setName("cafe");
        Guava6.setImage(R.drawable.logo);
        fruitList.add(Guava6);


        Fruit Guava7 = new Fruit();
        Guava7.setName("church");
        Guava7.setImage(R.drawable.logo);
        fruitList.add(Guava7);


        Fruit Guava8 = new Fruit();
        Guava8.setName("post_office");
        Guava8.setImage(R.drawable.logo);
        fruitList.add(Guava8);


        Fruit Guava9 = new Fruit();
        Guava9.setName("doctor");
        Guava9.setImage(R.drawable.logo);
        fruitList.add(Guava9);

        Fruit Guava10 = new Fruit();
        Guava10.setName("police");
        Guava10.setImage(R.drawable.logo);
        fruitList.add(Guava10);

        Fruit Guava11 = new Fruit();
        Guava11.setName("supermarket");
        Guava11.setImage(R.drawable.logo);
        fruitList.add(Guava11);


        Fruit Guava12 = new Fruit();
        Guava12.setName("university");
        Guava12.setImage(R.drawable.logo);
        fruitList.add(Guava12);


        return fruitList;
    }

}
