//main.c

#include <stdio.h>
#include "agents.h"

int main (void)
{
	struct agent Bob = newagent (0 , 0) ;
	struct agent Alice = newagent (3 , 3) ;
	north_remembers (& Bob );
	south (& Alice );
	west (& Alice );
	north_remembers (& Bob );
	east (& Bob) ;
	south (& Alice );
	printf ("odległość = %f\n", distance (Bob , Alice ));
	return 0;
}
