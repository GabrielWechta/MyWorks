#include <stdio.h>

struct agent
{
	int x;
	int y;
};

struct agent newagent(int a, int b)
{
	struct agent Bourne;
	Bourne.x = a;
	Bourne.y = b;

	return Bourne;
}

void south(struct agent *a)
{
 a->y--;
}

int main(void)
{
	struct agent Bob = newagent(1,0);	
	printf("%i ,  %i", Bob.x, Bob.y);
	struct agent* wsk_na_Boba = &Bob;
	south(wsk_na_Boba);
	printf("%i ,  %i", Bob.x, Bob.y);


return 0;
}
