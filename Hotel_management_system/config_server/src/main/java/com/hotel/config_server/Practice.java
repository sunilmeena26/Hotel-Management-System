package com.hotel.config_server;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//class Student{
//    public Student(){
//
//    };
//
//    public Student(Student s){
//        s1=s;
//
//    }
//    public Student(int a){
//        this.age=a;
//    }
//    Student s1;
//    int age;
//
//}

public class Practice {

 public  static String getGrade(double percentage){
     if(percentage>90){
                return "A+";
            } else  if(percentage>70&&percentage<90){
         return "A";
            } else  if(percentage>60&&percentage<70){
         return "B";
            } else{
         return "C";
            }
 }

    public static void main(String[] args) {


        Map<String,Integer> map=new HashMap<>();
        map.put("Rucha", 80);
        map.put("Sunil",70);

        List<Integer> list= Arrays.asList(10,28,3,8);

        Integer desList=list.stream().sorted(Collections.reverseOrder()).findFirst().get();

        System.out.println(desList);

        Integer n=list.stream().sorted().findFirst().get();
        System.out.println();
       Map<String,String> map2=map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,value->getGrade(value.getValue())));

        System.out.println(map2);

//        for(Map.Entry<String,Integer> entry:map.entrySet()){
//            if(entry.getValue()>90){
//                newMap.put(entry.getKey(),"A+");
//            } else  if(entry.getValue()>70&&entry.getValue()<90){
//                newMap.put(entry.getKey(),"A");
//            } else  if(entry.getValue()>60&&entry.getValue()<70){
//                newMap.put(entry.getKey(),"B");
//            } else{
//                newMap.put(entry.getKey(),"C");
//            }
//        }



       String name ="Hello";

        Map<Character, Long> map1=name.chars().mapToObj(c->(char)c).collect(Collectors.groupingBy(k->k,Collectors.counting()));

        System.out.println(map1);



        int [] a = {2,5,1,6,0,4};





        }




    }
