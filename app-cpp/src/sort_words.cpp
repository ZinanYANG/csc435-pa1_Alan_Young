#include <iostream>
#include <string>

void sort_words(std::string input_dir, std::string output_dir, std::size_t& num_words, double& execution_time)
{
    // TO-DO implement sort_words logic
}

int main(int argc, char** argv)
{
    std::size_t num_words = 0;
    double execution_time = 0.0;

    if (argc != 3) {
        std::cerr << "improper number of arguments" << std::endl;
        return 1;
    }

    sort_words(std::string(argv[1]), std::string(argv[2]), num_words, execution_time);

    std::cout << "Finished sorting " << num_words << " words";
    std::cout << " in " << execution_time << " miliseconds" << std::endl;

    return 0;
}