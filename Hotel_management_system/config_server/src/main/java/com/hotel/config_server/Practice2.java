package com.hotel.config_server;

import java.util.Map;
import java.util.stream.Collectors;

public class Practice2 {
      public static int count=0;
    public static int temp;
        public static int fib(int n){
          temp=n;
            if(count==temp){
                return 1;
            }

            int a=n; //5
            int b=n+1;  //6
            if(temp==n){
                count--;
                System.out.print(a+" "+b);

                return fib(b);   //6
            }
            count--;
            
            System.out.print((b+n)+" ");
            a=b;
           b=b+n;
            return fib(b);

        }

        public static void main(String[] args) {
//            System.out.println("Try programiz.pro");
//            String str="Meena";
//
//            Map<Character, Long> map=str.chars().mapToObj(c->(char)c).collect(Collectors.groupingBy(k->k,Collectors.counting()));
//
//            System.out.print(map);
//            System.out. println();
//             for(Map.Entry<Character,Long> set:map.entrySet()){
//                if(set.getValue()==1){
//
//                    System.out.print(set.getKey());
//               }
//            }

          int a=  fib(5);

        }
    }

