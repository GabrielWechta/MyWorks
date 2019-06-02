//agents.h

struct agent
{
	int x;
	int y;
};

struct agent newagent(int a, int b);
void north_remembers(struct agent* a);
void south(struct agent* a);
void west(struct agent* a);
void east(struct agent* a);
double distance(struct agent a1, struct agent a2);
