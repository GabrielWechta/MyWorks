#Makefile

all: main
main:main.o distance.o east.o newagent.o north_remembers.o south.o west.o
	clang -o main main.o distance.o east.o newagent.o north_remembers.o south.o west.o -lm
main.o:main.c
	clang -c main.c
newagent.o:newagent.c
	clang -c newagent.c
distance.o:distance.c
	clang -c distance.c
east.o:east.c
	clang -c east.c
west.o:west.c
	clang -c west.c
south.o:south.c
	clang -c south.c
north_remembers.o:north_remembers.c
	clang -c north_remembers.c
clean:
	rm -f main *.o *~
