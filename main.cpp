#include <string>
#include <iostream>
#include <filesystem>
namespace fs = std :: filesystem;
using namespace std;

void print_dir(string dir_path, int dir_iterator){

for(auto entry : filesystem::directory_iterator(dir_path))
{
    for(int i = 0; i < dir_iterator; i++){
        cout << "\t";
    }
    if(entry.is_directory()){
        cout <<"[d]" << entry.path().filename().string() << endl;
        print_dir(entry.path(), dir_iterator + 1);
    }
    else{
        cout <<"[f]" << entry.path().filename().string() << endl;
    }
}
}

int main(){
    string path = filesystem::current_path();
    print_dir(path, 0);
    return 0;
}
