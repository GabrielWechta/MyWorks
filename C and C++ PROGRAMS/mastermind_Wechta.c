#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

int main()
{
    int possitive[4][1296], possitive_counter=1296, black=0, white=0, round=1;
    bool bool_possitive[1296];
    srand(time(NULL));
    for (int i=0;i<1296;i++) ///petla generujaca cala tablice
    {
        bool_possitive[i]=true;
        int helper=i;
        for (int j=0;j<4;j++)
        {
            possitive[j][i]=0; ///przygotowanie petli
            possitive[j][i]+=helper%6 + 1; ///mozna sprobowac podniesc o 1
            helper/=6;
        }
    }
    printf("Welcome to MasterMind!\n");
    while (black!=4)
    {
        if (possitive_counter==0)
        {
            printf("\nOszukujesz!");
            break;
        }
        printf("\n%d: ",round);
        round++;
        int current = rand()%1296;
        while (bool_possitive[current] == 0) current = rand()%1296;
        bool_possitive[current]=false;
        possitive_counter--;
        for (int i=0;i<4;i++) ///wypisanie naszego losu
        {
            printf("%d ",possitive[i][current]);
        }
        printf("\nblack: ");
        scanf("%d", &black);
        printf("white: ");
        scanf("%d", &white);
        if (black==4)
        {
            printf("\nI won. My logic is unendible. \n");
            break;
        }
        for (int i=0;i<1296;i++)
        {
            if (bool_possitive[i] == 0) continue;
            int black_how_much=0, white_how_much=0, current_copy[4], current_possitive_copy[4];
            for (int j=0;j<4;j++)
            {
                current_copy[j]=possitive[j][current];
                current_possitive_copy[j]=possitive[j][i];
                if (current_possitive_copy[j]==current_copy[j])
                {
                    black_how_much++;
                    current_copy[j]=7;
                    current_possitive_copy[j]=7;
                }
            }
            for (int j=0;j<4;j++)
            {
                if (current_possitive_copy[j]<7)
                {
                    for(int k=0;k<4;k++)
                    {
                        if (current_possitive_copy[j]==current_copy[k])
                        {
                            white_how_much++;
                            current_copy[k]=7;
                            break;
                        }
                    }
                }
            }
            if (!(white_how_much==white && black_how_much==black))
            {
                bool_possitive[i]=false;
                possitive_counter--;
            }
        }
    }
    return 0;
}
