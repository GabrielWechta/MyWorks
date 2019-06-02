#include <stdio.h>
typedef int bool;
#define true 1
#define false 0

int length_f(char* lancuch)
{
    int dlugosc = 0;
    while(lancuch[dlugosc] != '\0') dlugosc++;
    return dlugosc - 1 ;
}

bool match(char* wzorzec, char* lancuch)
{
   int wzorzec_len = length_f(wzorzec);
   int lancuch_len = length_f(lancuch);

   int i = 0, j = 0, lancuch_paluch = -1, wzorzec_paluch = -1;

   if (wzorzec_len == 0) return (lancuch_len == 0);

   while (i < lancuch_len)
   {
      if (lancuch[i] == wzorzec[j]) ///obcinanie od lewej
      {
         i++;
         j++;
      } else if (j < wzorzec_len && wzorzec[j] == '?') ///obcinanie od lewej z '?'
      {
         i++;
         j++;
      } else if (j < wzorzec_len && wzorzec[j] == '*') ///zrzucenie paluchow
      {
         lancuch_paluch = i;
         wzorzec_paluch = j;
         j++;
      } else if (wzorzec_paluch != -1) ///powrot do ostatniej gwiazdki i szukanie dalej
      {
         j = wzorzec_paluch + 1;
         i = lancuch_paluch + 1;
         lancuch_paluch++;
      }
        else return false;
   }

   while (j < wzorzec_len && wzorzec[j] == '*') j++; ///na wypadek pustych gwiazdek pod koniec

   if (j == wzorzec_len) return true;

   return false;
}

int main()
{
   char wzo[100], lan[100];
   printf("Wpisz wzorzec i lancuch \n");
   fgets(wzo,100,stdin);
   fgets(lan,100,stdin);

   if (match(wzo, lan))
      puts("TAK");
   else
      puts("NIC Z TYCH RZECZY");
}
