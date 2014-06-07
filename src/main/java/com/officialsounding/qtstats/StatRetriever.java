package com.officialsounding.qtstats;


import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class StatRetriever {

    public static void main(String[] args) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<QT> locations = new ArrayList<>();

        try {
            builder = factory.newDocumentBuilder();
            //find all QuikTrips in a 5 mile radius of 74135 (aka roughly the center of Tulsa)
            Document doc = builder.parse("http://hosted.where2getit.com/quiktrip/ajax?&xml_request=%3Crequest%3E%3Cappkey%3E82C11D38-0EC6-11E0-8AD9-4C59241F5146%3C%2Fappkey%3E%3Cformdata+id%3D%22locatorsearch%22%3E%3Cdataview%3Estore_default%3C%2Fdataview%3E%3Climit%3E200%3C%2Flimit%3E%3Cgeolocs%3E%3Cgeoloc%3E%3Caddressline%3E74135%3C%2Faddressline%3E%3Clongitude%3E%3C%2Flongitude%3E%3Clatitude%3E%3C%2Flatitude%3E%3C%2Fgeoloc%3E%3C%2Fgeolocs%3E%3Csearchradius%3E5%3C%2Fsearchradius%3E%3Cwhere%3E%3Ctravelcenter%3E%3Ceq%3E%3C%2Feq%3E%3C%2Ftravelcenter%3E%3Ctruckdiesel%3E%3Ceq%3E%3C%2Feq%3E%3C%2Ftruckdiesel%3E%3Cautodiesel%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fautodiesel%3E%3Cspecialtydrinks%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fspecialtydrinks%3E%3Cgen3%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fgen3%3E%3Chotandfreshpretzels%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fhotandfreshpretzels%3E%3Cnoethanol%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fnoethanol%3E%3Ccertifiedscales%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fcertifiedscales%3E%3Cdef%3E%3Ceq%3E%3C%2Feq%3E%3C%2Fdef%3E%3Cfrozentreats%3E%3Ceq%3E%3C%2Feq%3E%3C%2Ffrozentreats%3E%3C%2Fwhere%3E%3C%2Fformdata%3E%3C%2Frequest%3E");
            NodeList qts = doc.getElementsByTagName("poi");
            System.out.println(qts.getLength());


            // excluding 51st & Union, since it is on the far side of the river and makes the farthest point in the rive itself
            for(int i = 0, len = 28; i < len; i++){
                Node qt = qts.item(i);
                if(qt instanceof Element) {
                    Element qtEl = (Element) qt;
                    QT value = new QT();
                    value.setIntersection(qtEl.getElementsByTagName("intersection").item(0).getTextContent());
                    value.setLatitude(Double.parseDouble(qtEl.getElementsByTagName("latitude").item(0).getTextContent()));
                    value.setLongitude(Double.parseDouble(qtEl.getElementsByTagName("longitude").item(0).getTextContent()));


                    locations.add(value);
                }
            }

            QT zero = new QT();
            QT max = new QT();

            zero.setX(0);
            zero.setY(0);


            //these are hardcoded at the moment, should probably just be found
            zero.setLongitude(-96.010);
            zero.setLatitude(36.030);

            max.setLongitude(-95.84);
            max.setLatitude(36.170);

            for(QT a: locations) {
                a.setX(a.getLatitude() - zero.getLatitude());
                a.setY(a.getLongitude() - zero.getLongitude());
            }


            max.setX(max.getLatitude() - zero.getLatitude());
            max.setY(max.getLongitude() - zero.getLongitude());

            for(QT a: locations) {
                System.out.println(a.getLatitude()+" "+a.getLongitude());
                System.out.println(a.getX() +" "+a.getY());
                System.out.println("+++");
            }


            double maxradius = 0;
            QT farthest = new QT();

            //for each trio of points, calculate the circle defined by those 3 points
            for(QT a : locations) {
                for(QT b: locations) {
                    for(QT c: locations) {

                        if(!a.equals(b) && !b.equals(c) && !a.equals(c)) {
                            double x1 = a.getX();
                            double x2 = b.getX();
                            double x3 = c.getX();

                            double y1 = a.getY();
                            double y2 = b.getY();
                            double y3 = c.getY();

                            double mr = (y2 - y1) / (x2 - x1);
                            double mt = (y3 - y2) / (x3 - x2);


                            double x = (mr * mt * (y3 - y1) +(mr * (x2 + x3)) - mt*(x1 + x2)) / (2 * (mr - mt));
                            double y = (-1 / mr) * (x - ((x1 + x2)/2)) + ((y1 + y2) / 2);

                            double r = sqrt(pow((x2 - x1),2) + pow((y2 - y1),2));

                            //validate

                            //inside of the bounds
                            boolean inside = (x >= 0.0) && (y >= 0.0) && (x <= max.getX()) && (y <= max.getY());
                            boolean empty = true;


                            //no other QTs inside of the circle
                            for(QT d: locations) {
                                if(d != a && d != b && d != c && pow(d.getX() - x,2) + pow(d.getY() - y,2) < pow(r,2)) {
                                    empty = false;
                                    break;
                                }
                            }

                            //if largest radius found so far, record
                            if(inside && empty && r > maxradius) {
                                maxradius = r;
                                farthest = new QT();
                                farthest.setX(x);
                                farthest.setY(y);
                                farthest.setIntersection(a.getIntersection()+" and "+b.getIntersection()+" and "+c.getIntersection());
                            }
                        }
                    }
                }
            }


            //display intersection set, lat & long of farthest point
            System.out.println(farthest.getIntersection());
            System.out.println((zero.getLatitude() + farthest.getX())+","+(zero.getLongitude() + farthest.getY()));
            System.out.println(maxradius);




        } catch (ParserConfigurationException | SAXException | IOException e) {
        }
    }
}
