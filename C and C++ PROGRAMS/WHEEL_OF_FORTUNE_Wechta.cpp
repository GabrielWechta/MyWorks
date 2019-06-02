#include <iostream>
#include <fstream>
#include <cstdlib>
#include <time.h>
#include <vector>
#include <conio.h>
#include <windows.h>
#include <cmath>
#include <string>

#define ilosc_hasel 5
#define ilosc_kategorii 3
#define ilosc_nagrod 8
using namespace std;

struct haslo_struktura
{
    string litery;
    vector <bool> czy_wyswietlac;
};

struct nagroda_struktura
{
    int pieniadze;
    string rzeczowa;
    bool czy_pieniadze;
};

struct gracz_struktura
{
    string nazwa;
    int suma_pieniedzy;
    vector <string> suweniry;
};

bool sprawdzanie_czy_spolgloska(char x)
{
    if( x == 'A' || x == 'E' || x == 'Y' || x == 'O' || x == 'I' || x == 'U' ) return false;
    return true;
}

bool sprawdzanie_czy_trafiono_haslo(string odpowiedz, haslo_struktura oryginal_hasla)
{
    //cout<<endl<<"  "<<oryginal_hasla.litery<<endl;
    //cout<<endl<<"  "<<odpowiedz<<endl;
    if(odpowiedz.length() != oryginal_hasla.litery.length()) return false;

    for(int i = 0; i < odpowiedz.length(); i++)
    {
        if(odpowiedz[i] != oryginal_hasla.litery[i]) return false;
    }

    return true;
}

nagroda_struktura losowanie ()
{
    nagroda_struktura nagroda;
    double czas = 0;
    int czas_sumaryczny = 0;
    double x = 0;
    ///miejsce na zajebista animacje

        Sleep(2000);
        cout<<endl<<"Przystepujemy do losowania";
        Sleep(2000);
        //system("cls");
        Sleep(500);

        for(int i = rand()%ilosc_nagrod+1 ; czas_sumaryczny < 6000; i++)
        {
            int zakres = i%ilosc_nagrod+1;
            switch(zakres)
            {
                case 1: nagroda.pieniadze = 500;
                        nagroda.czy_pieniadze = true;
                        break;
                case 2: nagroda.pieniadze = 300;
                        nagroda.czy_pieniadze = true;
                        break;
                case 3: nagroda.pieniadze = 1500;
                        nagroda.czy_pieniadze = true;
                        break;
                case 4: nagroda.rzeczowa = "LADNY TELEWIZOR";
                        nagroda.czy_pieniadze = false;
                        break;
                case 5: nagroda.pieniadze = 600;
                        nagroda.czy_pieniadze = true;
                        break;
                case 6: nagroda.rzeczowa = "JEZ Z KLATKA";
                        nagroda.czy_pieniadze = false;
                        break;
                case 7: nagroda.rzeczowa = "BILET NA PEARL JAM";
                        nagroda.czy_pieniadze = false;
                        break;
                case 8: nagroda.pieniadze = 200;
                        nagroda.czy_pieniadze = true;
                        break;
                default : cout<<"zepsol sie switch";
                        break;
            }
            if(nagroda.czy_pieniadze)cout<<endl<<"     "<<nagroda.pieniadze;//<<"     "<<x<<"    "<<czas;
            else cout<<endl<<"     "<<nagroda.rzeczowa;
            x++;
            //if(x<10) czas = pow(x,3);
            czas = x*20;
            Sleep(czas); ///do poprawienia
            //system("cls");
            czas_sumaryczny += czas;

        }
        return nagroda;
}

int losowanie_wypisywancyh_liter(haslo_struktura &haslo)  ///funkcja zwraca pierwsze wypisanie hasla
{
    bool czy_wysweitlac[haslo.litery.length()];
    for(int y = 0 ; y < haslo.litery.length(); y++)
    {
        haslo.czy_wyswietlac.push_back(false);
    }
    for(int i = 0; i < haslo.litery.length()/4; i++)
    {
        int losowa = rand()%haslo.litery.length();
        haslo.czy_wyswietlac[losowa] = true;
    }
}

void wypisanie_hasla(haslo_struktura haslo, string kategoria)
{
    cout<<endl<<"Kategoria pytania to: "<<kategoria<<"     "<<endl<<endl<<"\t\t";

    for(int y = 0 ; y < haslo.litery.length(); y++)
    {
        if(haslo.czy_wyswietlac[y]) cout<<" "<<haslo.litery[y]<<" ";
        else if(haslo.litery[y] == 32 ) cout<<"   ";
        else cout<<" _ ";
    }
}

void szukanie_liter_zaznaczanie_przypisywanie_nagrody(haslo_struktura &haslo, string odpowiedz, bool &czy_padlo, gracz_struktura &gracz, nagroda_struktura nagroda)
{
    czy_padlo = false;
    int counter_trafionych = 0;
    for(int i = 0 ; i < haslo.litery.length(); i++)
    {
        if(haslo.litery[i] == odpowiedz[0] && haslo.czy_wyswietlac[i] == false)
        {
            haslo.czy_wyswietlac[i] = true;
            czy_padlo = true;
            counter_trafionych++;
        }
    }
    if(nagroda.czy_pieniadze && czy_padlo)
    {
        cout<<endl<<"Trafiles tyle liter: "<<counter_trafionych<<"! Wygrywasz "<<counter_trafionych * nagroda.pieniadze<<endl;
        gracz.suma_pieniedzy += counter_trafionych * nagroda.pieniadze;
    }
    if(!nagroda.czy_pieniadze && czy_padlo)
    {
        gracz.suweniry.push_back(nagroda.rzeczowa);
        cout<<endl<<"Gratulacje! Twoja nagroda to "<<nagroda.rzeczowa<<endl;
    }
}

void zgadywanie(gracz_struktura &gracz, haslo_struktura &haslo, bool &czy_padlo, bool &koniec_rundy, string kategoria, haslo_struktura oryginal_hasla)
{

    nagroda_struktura nagroda = losowanie();
    cout<<endl<<"NAGRODA: ";
    if(nagroda.czy_pieniadze) cout<<nagroda.pieniadze;
    else cout<<nagroda.rzeczowa;

    cout<<"\t\t"<<"Gracz: "<<gracz.nazwa<<"\tPieniadze: "<<gracz.suma_pieniedzy<<endl;
    cout<<"\t\t";

    wypisanie_hasla(haslo, kategoria);
    cout<<endl<<endl<<"twoja odpowiedz to: ";
    string odpowiedz;

    getline(cin, odpowiedz);
    cout<<endl<<odpowiedz<<endl;
    if(odpowiedz.length() == 1 && sprawdzanie_czy_spolgloska(odpowiedz[0])) szukanie_liter_zaznaczanie_przypisywanie_nagrody(haslo, odpowiedz, czy_padlo, gracz, nagroda);
    if(odpowiedz.length() == 1 && !sprawdzanie_czy_spolgloska(odpowiedz[0]))
    {
        cout<<endl<<"Podana litera nie jest spolgloska (ajajajj fatalny blad)"<<endl;
        czy_padlo = false;
    }
    if(odpowiedz.length() != 1)
    {
        cout<<(int)sprawdzanie_czy_trafiono_haslo(odpowiedz, oryginal_hasla);
        if(sprawdzanie_czy_trafiono_haslo(odpowiedz, oryginal_hasla)) koniec_rundy = true;
        czy_padlo = false;
    }
}

int main()
{
    srand (time(NULL));

    gracz_struktura tablica_graczy[2];      ///tablica graczy xd

            for(int numer_gracza = 0; numer_gracza < 2; numer_gracza++) ///stowrzenie graczy alleluja
            {
                cout<<"Podaj imie gracza numer "<<numer_gracza<<endl;
                getline(cin,tablica_graczy[numer_gracza].nazwa);
                tablica_graczy[numer_gracza].suma_pieniedzy = 0;
            }

    for(int numer_rundy = 1; numer_rundy <= 2; numer_rundy++)
    {
        cout<<"\tRUNDA "<<numer_rundy;
        Sleep(2000);
        //system("cls");

     ///miejsce na bajery z wczytywaniem
        fstream plik_hasla;
        plik_hasla.open("hasla.txt" , ios::in);
        if(!plik_hasla.good()) cout<<"brakuje pliku z haslami"<<endl;
        int los_kategorii = rand() % ilosc_kategorii;
        int los_pytania = rand() % ilosc_hasel + 2;

        haslo_struktura haslo;
        string kategoria;

        for(int i = 0; i < los_kategorii * 6 + los_pytania; i++) ///petla losujaca pytanie i kategorie
        {
            getline(plik_hasla, haslo.litery);          ///tutaj wczytwane sa hasla do czasu az nie wczyta odpowiedniego
            if(i%6 == 0) kategoria = haslo.litery;      ///tutaj wybierana jest kategoria pytania
        }

        Sleep(3000);
        //system("cls");
        haslo_struktura oryginal_hasla = haslo;
        losowanie_wypisywancyh_liter(haslo);
        wypisanie_hasla(haslo, kategoria);

        bool czy_padlo = true;
        bool koniec_rundy = false;
        gracz_struktura gracz_1;
        int numer_gracza = 0;
        bool flaga = false;

        while(!koniec_rundy)
        {
            numer_gracza = numer_gracza%2 ;
            while(1)
            {
                zgadywanie(tablica_graczy[numer_gracza], haslo, czy_padlo, koniec_rundy, kategoria, oryginal_hasla);
                if(!koniec_rundy)
                    {
                       if(!czy_padlo)
                        {
                            cout<<endl<<"\t\tNiestety brak takiej litery"<<endl;
                            break;
                        }
                        wypisanie_hasla(haslo, kategoria);
                    }
                if(koniec_rundy)
                    {
                        Sleep(2000);
                        system("cls");
                        cout<<"\t\t"<<"Gracz: "<<tablica_graczy[numer_gracza].nazwa<<"\tPieniadze: "<<tablica_graczy[numer_gracza].suma_pieniedzy<<"\tSuweniry: "<<endl;
                        for(int nr_suwenira; nr_suwenira < tablica_graczy[numer_gracza].suweniry.size(); nr_suwenira++) cout<<tablica_graczy[numer_gracza].suweniry[nr_suwenira]<<"  ";
                        cout<<"\t\t";
                        cout<<endl<<"\t\t GRATULACJE UTOPIONE W KILKU WYKRZYKNIKACH"<<endl;
                        int przegrywajacy;
                        if(numer_gracza == 1 ) przegrywajacy = 0;
                        else przegrywajacy = 1;
                        tablica_graczy[przegrywajacy].suma_pieniedzy = 0;
                        tablica_graczy[przegrywajacy].suweniry.empty();
                        Sleep(2000);
                        system("cls");
                        break;
                    }
                }
            cout<<endl<<"\t\tKOLEJNY GRACZ";
            Sleep(2000);
            numer_gracza++;
            system("cls");
            }

        }
        cout<<"\tWYNIKI:"<<endl;
        for(int i = 0; i < 2; i++)
        {
            cout<<"Gracz: "<<tablica_graczy[i].nazwa<<endl<<"Suma pieniedzy: "<<tablica_graczy[i].suma_pieniedzy<<endl<<"Suweniry: ";
            for(int nr_suwenira; nr_suwenira < tablica_graczy[i].suweniry.size(); nr_suwenira++) cout<<tablica_graczy[i].suweniry[nr_suwenira]<<"  ";
            cout<<endl;
        }

    return 0;
}
