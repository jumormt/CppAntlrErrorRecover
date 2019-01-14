#include <string>
using namespace std;

void test(char *str)
{
	int MAXSIZE=40;
	char buf[MAXSIZE];
	if(!buf)
		return;
	strcpy(buf, str); /*string copy*/
}

int main(int argc, char **argv)
{
	char *userstr;
	if(argc > 1) {
		userstr = argv[1];
		test(userstr,ur,ur,test(ur));
	}
	return 0;
}
