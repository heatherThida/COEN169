#
# Script that is used to create test data files from training data. The usage
# of the script is the following:
#
#   python create_test_data_from_train.py <training file input path>
#          <train file output path> <test file output path>
#          <test answer output path>

import sys

def read_train(train_data_path):
    train_data = {}
    with open(train_data_path, 'r') as f:
        for ii, line in enumerate(f.readlines()):
            user_movie_ratings = dict(
                [(i + 1, ratings)
                 for i,ratings in enumerate(map(int,line.split())) if ratings !=0 ])
            train_data[ii] = user_movie_ratings
    return train_data

def main():
    if len(sys.argv) < 5:
        print("Please provide a training file input path and output paths for "
              "a new training file, a test file, and a file containing the "
              "answers for the test data.")

    input_train_data_path = sys.argv[1]
    output_train_data_path = sys.argv[2]
    output_test_data_path = sys.argv[3]
    answer_data_path = sys.argv[4]

    train_data = read_train(input_train_data_path)

    # Select half of the users to write into the training data and the other
    # half to use for test data.
    train_data_items_list = list(train_data.items())
    train_data_half_len = int(len(train_data_items_list)/2)
    with open(output_train_data_path, "w") as train_data_fd:
        for user, user_movie_ratings in train_data_items_list[:train_data_half_len]:
            train_data_fd.write(" ".join(
                [ str(rating)
                  for _, rating in user_movie_ratings.items() ]))
            train_data_fd.write("\n")

    # With the other half write out the answers file and the test file.
    num_unknown = 0
    with open(output_test_data_path, "w") as output_data_fd:
        with open(answer_data_path, "w") as answer_data_fd:
            for user, user_movie_ratings in train_data_items_list[train_data_half_len:]:
                # Split the movie ratings in half, leave the first half as test
                # data and the other half as unknown.
                unknown_after_idx = int(len(user_movie_ratings)/2)
                for ii, (movie, rating) in enumerate(user_movie_ratings.items()):
                    if ii < unknown_after_idx:
                        output_data_fd.write("%s %s %s\n" %
                                                    (user, movie, rating))
                    else:
                        num_unknown += 1
                        output_data_fd.write("%s %s 0\n" % (user, movie))
                        answer_data_fd.write("%s %s %s\n" %
                                             (user, movie, rating))
    print("Num unknown %s" % num_unknown)

if __name__ == "__main__":
  main()
