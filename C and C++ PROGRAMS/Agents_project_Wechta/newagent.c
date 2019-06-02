//newagent.h

#include "agents.h"

struct agent newagent(int a, int b)
{
	struct agent Bourne;
	Bourne.x = a;
	Bourne.y = b;

	return Bourne;
}
