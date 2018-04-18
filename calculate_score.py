#
# Calculates the score of a results file. The usage is the following:
#
#   python calculate_score.py <result path> <answer path>
#

from collections import defaultdict
import math
import sys

def read_data(filename):
    with open(filename,"r") as f1:
        data = defaultdict(dict)
        for line in f1.readlines():
           test_cols = line.split()
           users = int(test_cols[0])
           movies = int(test_cols[1])
           ratings = int(test_cols[2])
           data[users][movies] = ratings
        return data

def main():
    if len(sys.argv) < 3:
        print("Please provide a path to a result file with predictions and an "
              "answer file")

    result_path = sys.argv[1]
    answer_path = sys.argv[2]

    result_data = read_data(result_path)
    answer_data = read_data(answer_path)

    squared_diff_sum = 0
    num_predicted_ratings = 0
    for user, predicted_user_movie_ratings in result_data.items():
        # We took half of each user's ratings as known and the other half as
        # known.
        num_predicted_ratings += len(predicted_user_movie_ratings)
        actual_user_movie_ratings = answer_data[user]
        for movie, predicted_rating in predicted_user_movie_ratings.items():
            actual_rating = actual_user_movie_ratings[movie]
            squared_diff_sum += (predicted_rating - actual_rating) ** 2

    # A little bit of a hack here, but since we are dividing the ratings in
    # half between known and unknown ratings, just hard code this.
    root_mean_squared_error = math.sqrt(
            (float(1)/num_predicted_ratings) * float(squared_diff_sum))

    print("Num predicted: %s" % num_predicted_ratings)
    print("Error is %s" % root_mean_squared_error)

if __name__ == "__main__":
    main()
