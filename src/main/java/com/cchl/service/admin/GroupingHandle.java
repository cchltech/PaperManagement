package com.cchl.service.admin;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GroupingHandle {
    public void setGroup(Integer teacherNumber ,Integer groupNumber) {
        int group = 0; //已答辩组数，初始化为0
        int limit = 3;
        int teacher[][] = new int[teacherNumber][1]; //存放每个老师分配到的组数
        Random rand = new Random();
        boolean[] bool = new boolean[teacherNumber];
        int randInt = 0;

        do {
            for(int i=0;i<teacherNumber;i++) {
                bool[i]=false;
            }
            System.out.println("\n随机选出来的老师为:");
            for(int i=0;i<4;i++) {

                do {
                    randInt  = rand.nextInt(teacherNumber);
                    int number =teacher[randInt][0];
                    if (number >= limit) {
                        bool[randInt]=true;
                    }
                }while(bool[randInt]);

                teacher[randInt][0]++;
                bool[randInt] = true;

                System.out.print(randInt+1+" ");
            }
            group++;
        }while(group < groupNumber);
        System.out.println("\n每个老师负责的组数为");
        for(int j=0;j<teacherNumber;j++) {
            System.out.print(teacher[j][0]+" ");
        }
    }

}
