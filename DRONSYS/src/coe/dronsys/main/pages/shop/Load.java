package coe.dronsys.main.pages.shop;

import coe.dronsys.main.pages.shop.model.Product;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Insert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jesus on 3/15/16.
 */
public class Load {
    private static List<Product> getProducts() throws Exception {

        Product p1 = new Product("Sunglases", 70.03, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/sunglases.jpg")), "jpg");
        Product p2 = new Product("Drone wings", 23, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/droneWings.jpg")), "jpg");
        Product p3 = new Product("Batteries", 66.5, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/battery.jpg")), "jpg");
        Product p4 = new Product("Gloves", 66.5, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/gloves.jpg")), "jpg");
        Product p5 = new Product("Remote control", 66.5, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/remoteControl.jpg")), "jpg");
        Product p6 = new Product("Fan", 66.5, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/fan.jpg")), "jpg");
        Product p7 = new Product("Camera", 66.5, Files.readAllBytes(Paths.get("/home/jesus/Projects/EWT/DRONSYS/load/camera.jpg")), "jpg");

        return Arrays.<Product>asList(p1, p2, p3, p4, p5, p6, p7);
    }

    public static void main(String[] args) throws Exception {
        DAO dao = DAO.get("DRONSYS-DB");
        for (Product p : getProducts())
            dao.execute(new Insert(p));
    }
}
