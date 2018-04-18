# Thida Aung
# Project II CSCI 169 Prof. Yi Fang


from collections import defaultdict
import sys
import numpy as np
import random
import csv
import json
import math

#   Load data from a input file into memory with dictionary format.

def read_train(train_user_data_path):
    num_total_users = 0
    with open(train_user_data_path, 'r') as f:
        train_user_data = defaultdict(dict)
        train_movie_data = defaultdict(dict)
        user_average_rating_dict = {}
        movie_rating_count_map = defaultdict(int)
        for user, line in enumerate(f.readlines()):
            num_total_users += 1
            for ii, rating_str in enumerate(line.split()):
                movie = ii + 1
                rating = int(rating_str)
                if rating == 0:
                    continue
                train_user_data[user][movie] = rating
                train_movie_data[movie][user] = rating

            #[{movie:rating},{}]
            ratings_sum = 0
            num_ratings = 0
            for movie, rating in train_user_data[user].items():
                ratings_sum += rating
                num_ratings += 1
                movie_rating_count_map[movie] += 1
            user_average_rating_dict[user] = (
                    float(ratings_sum)/float(num_ratings))

        # Adjust ratings with IUF.
        iuf_movie_dict = {}
        for movie, num_movie_ratings in movie_rating_count_map.items():
            iuf_movie_dict[movie] = math.log(
                    len(train_user_data)/num_movie_ratings)

        for train_user in train_user_data.values():
            for movie in train_user:
                train_user_rating = train_user[movie]
                train_user_rating *= iuf_movie_dict.get(movie, 1)
                train_user[movie] = min(5.0, train_user_rating)
        return (train_user_data, user_average_rating_dict, train_movie_data,
                iuf_movie_dict)

def read_test(filename, train_movie_data, train_user_data,
              user_average_rating_dict, iuf_movie_dict):
    with open(filename,"r") as f1:
        test_user_data = {}
        for line in f1.readlines():
            test_cols = line.split()
            user = int(test_cols[0])
            movie = int(test_cols[1])
            rating = int(test_cols[2])
            # For IUF.
            #rating = int(min(5.0, iuf_movie_dict.get(movies, 1) * int(test_cols[2])))
            if user not in test_user_data:
                test_user_data[user] = ({}, {})
            known_rating, unknown_rating = test_user_data[user]
            if rating == 0:
                unknown_rating[movie] = rating
            else:
                known_rating[movie] = rating
                train_movie_data[movie][user] = rating
                train_user_data[user][movie] = rating

        for user, (known_ratings, _) in test_user_data.items():
            rating_sum = sum(known_ratings.values())
            user_average_rating_dict[user] = (
                    float(rating_sum)/len(known_ratings))
        return test_user_data

def above_threshold(train_user, test_user, threshold):
    """
    Computes whether the train user and test user share enough common ratings.
    """
    train_movie_set = set(train_user.keys())
    test_movie_set = set(test_user.keys())
    if len(train_movie_set.intersection(test_movie_set)) < threshold:
        return False
    else:
        return True

def similar_users_cosine(train_user_data, test_user_data,
                         user_average_rating_dict, movie_average_rating_dict,
                         k):
    similarity_map = {}

    for user, (known_ratings, unknown_ratings) in test_user_data.items():
       similar_users = []
       test_user_movies_seen = set(known_ratings.keys())
       for train_user, train_user_ratings in train_user_data.items():
            if user == train_user:
                continue
            if not above_threshold(train_user_ratings, known_ratings, 2):
                continue

            dot_product = 0
            user_ratings_sqr = 0
            train_user_ratings_sqr = 0

            train_user_average = user_average_rating_dict[train_user]
            test_user_average = user_average_rating_dict[user]
            for movie in range(1, 1001):
                movie_average_rating = movie_average_rating_dict.get(movie, 3)
                train_user_rating = train_user_ratings.get(
                        movie,
                        movie_average_rating)
                test_user_rating = known_ratings.get(movie,
                                                     movie_average_rating)
                dot_product += train_user_rating * test_user_rating
                user_ratings_sqr += test_user_rating ** 2
                train_user_ratings_sqr += train_user_rating ** 2

            denominator = (math.sqrt(user_ratings_sqr) *
                           math.sqrt(train_user_ratings_sqr))

            # If the train user has not rated any common movies with
            # test user than it is not similar, so we can just default
            # to 0.
            if denominator == 0:
                similarity_calculation = 0
            else:
                # Update 1 for the case based amplifier.
                similarity_calculation = math.pow(
                        float(dot_product)/float(denominator),
                        1)

            similar_users.append((
                train_user_ratings,
                similarity_calculation,
                user_average_rating_dict[train_user]))

       # Grab training users specifically for each of the unrated movies.
       similar_users = sorted(similar_users, key=lambda x: x[1])
       start_slice_idx = max(0, len(similar_users) - k)
       similarity_map[user] = similar_users[start_slice_idx:]
    return similarity_map

def predict_cosine(similarity_map, test_user_data, train_user_data,
                   user_average_rating_dict, movie_average_rating_dict):
    low_ratings = 0
    high_ratings = 0
    score_distribution = defaultdict(int)
    used_average = set()
    for user, (known_ratings, unknown_ratings) in test_user_data.items():
        #similar_users_movie_map = similarity_map[user]
        similar_users = similarity_map[user]
        for movie in unknown_ratings.keys():
            predicted_rating = 0
            denominator = 0
            similarity_list = []
            for train_user, similarity_calculation, train_user_average in \
                    similar_users:
                if movie not in train_user:
                    continue
                similarity_list.append(similarity_calculation)
                denominator += similarity_calculation
                train_rating_adj = train_user[movie]
                predicted_rating += (
                        similarity_calculation * float(train_rating_adj))

            # The denominator will be 0 if we cannot find any similar training
            # users with scores to base the test user's score from. In this
            # case just take an average.
            if denominator == 0:
                movie_average_rating = movie_average_rating_dict.get(movie, 3)
                used_average.add(movie)
                prediction = int(movie_average_rating)
            else:
                prediction = int(predicted_rating/denominator)

            prediction = min(5, max(1, prediction))
            score_distribution[prediction] += 1
            unknown_ratings[movie] = prediction

    print("Score distribution: %s" % score_distribution)
    print("High ratings: %s" % high_ratings)
    print("Low ratings: %s" % low_ratings)
    print("Movies defaulting to average: %s" % len(used_average))
    for movie in used_average:
        users_who_rated_movie = []
        for train_user, train_user_ratings in train_user_data.items():
            if movie in train_user_ratings:
                users_who_rated_movie.append(train_user)

def similar_users_pearson(train_user_data, test_user_data,
                          user_average_rating_dict, movie_average_rating_dict,
                          k):
    similarity_map = {}

    for user, (known_ratings, unknown_ratings) in test_user_data.items():
        test_user_average = user_average_rating_dict[user]
        similar_users = []
        for train_user, train_user_ratings in train_user_data.items():
            if train_user == user:
                continue
            if not above_threshold(train_user_ratings, known_ratings, 2):
                continue

            dot_product = 0
            test_user_ratings_sqr = 0
            train_user_ratings_sqr = 0
            train_user_average = user_average_rating_dict[train_user]
            # There are movies (1-1000).
            for movie in range(1, 1001):
                movie_average_rating = movie_average_rating_dict.get(movie,
                                                                     3)
                test_rating_adj = known_ratings.get(movie,
                                                    movie_average_rating)
                train_rating_adj = train_user_ratings.get(
                        movie,
                        movie_average_rating)
                dot_product += (
                        (train_rating_adj - train_user_average) *
                        (test_rating_adj - test_user_average))

                test_user_ratings_sqr += math.pow(
                        test_rating_adj - test_user_average, 2)
                train_user_ratings_sqr += math.pow(
                        train_rating_adj - train_user_average, 2)

            denominator = (math.sqrt(train_user_ratings_sqr) *
                           math.sqrt(test_user_ratings_sqr))
            if(denominator == 0):
                similarity_calculation = 0
            else:
                similarity_calculation = float(dot_product)/float(denominator)
                if similarity_calculation > 1:
                    similarity_calculation = 1.0
                elif similarity_calculation < -1:
                    similarity_calculation = -1.0

                # Modify pow for case based amplifier.
                adj_similarity_calculatio = math.pow(
                        math.fabs(similarity_calculation),
                        1)

            similar_users.append((
                train_user_ratings,
                similarity_calculation,
                user_average_rating_dict[train_user]))

        # Of the K similar users we select we will grab try to grab k/2
        # positively correlated users and k/2 negatively correlated users.
        sorted_similar_users = sorted(similar_users,
                                      key=lambda x: math.fabs(x[1]))
        start_slice_idx = max(0, len(sorted_similar_users) - k)
        similar_users_slice = sorted_similar_users[start_slice_idx:]
        similarity_map[user] = (similar_users_slice, test_user_average)
    return similarity_map

def predict_pearson(similarity_map, test_user_data, movie_average_rating_dict):
    low_ratings = 0
    high_ratings = 0
    used_average = 0
    score_distribution = defaultdict(int)
    for user, (known_ratings, unknown_ratings) in test_user_data.items():
        similar_users, test_user_average = similarity_map[user]
        for movie in unknown_ratings.keys():
            predicted_rating = 0
            denominator = 0
            for (train_user_ratings, similarity_calculation,
                 train_user_average) in similar_users:
                if movie not in train_user_ratings:
                    continue
                train_rating_adj = train_user_ratings[movie]
                denominator += math.fabs(similarity_calculation)
                rating_delta = train_rating_adj - train_user_average
                predicted_rating += (
                    float(similarity_calculation) *
                    float(rating_delta))
            if(denominator == 0):
                movie_average_rating = movie_average_rating_dict.get(movie, 3)
                unknown_ratings[movie] = int(movie_average_rating)
                used_average += 1
            else:
                float_prediction = (
                        test_user_average + (
                            predicted_rating/denominator))
                prediction = int(float_prediction)

                if prediction < 1:
                    unknown_ratings[movie] = 1
                    low_ratings += 1
                elif prediction > 5:
                    unknown_ratings[movie] = 5
                    high_ratings += 1
                else:
                    unknown_ratings[movie] = prediction
            score_distribution[unknown_ratings[movie]] += 1
    print("High ratings: %s" % high_ratings)
    print("Low ratings: %s" % low_ratings)
    print("Score distribution: %s" % score_distribution)
    print("Times we used the average: %s" % used_average)

def similarity_movies_adjusted_cosine(train_movie_data,
                                      user_average_rating_dict, k):
    # Mapping from a movie to the 'k' closest movies.
    similarity_map = {}
    cached_user_set_dict = {}
    similarity_cache_dict = {}
    for movie_ii, user_rating_map_ii in train_movie_data.items():
        similarity_scores = []
        if movie_ii in cached_user_set_dict:
            user_set_ii = cached_user_set_dict[movie_ii]
        else:
            user_set_ii = set(user_rating_map_ii.keys())
            cached_user_set_dict[movie_ii] = user_set_ii
        for movie_jj, user_rating_map_jj in train_movie_data.items():
            if movie_ii == movie_jj:
                continue

            # Skip computing a similarity again if we have already computed it.
            if (movie_jj, movie_ii) in similarity_cache_dict:
                similarity_score = similarity_cache_dict[(movie_jj, movie_ii)]
                similarity_scores.append(
                        (similarity_score, user_rating_map_jj))

            if movie_jj in cached_user_set_dict:
                user_set_jj = cached_user_set_dict[movie_jj]
            else:
                user_set_jj = set(user_rating_map_jj.keys())
                cached_user_set_dict[movie_jj] = user_set_jj

            numerator = 0
            movie_ii_rating_diff_sqrd = 0
            movie_jj_rating_diff_sqrd = 0
            # In order to iterate through all possible users use the keys from
            # 'user_average_dict'. The reason we do this is that so we don't
            # skip computing items for users that haven't rated movie_ii or
            # movie_jj and wouldn't be present in either of the user rating
            # maps.
            for user, rating_average in user_average_rating_dict.items():
                rating_ii = user_rating_map_ii.get(user, rating_average)
                rating_jj = user_rating_map_jj.get(user, rating_average)
                rating_ii_diff = rating_ii - rating_average
                rating_jj_diff = rating_jj - rating_average
                numerator += rating_ii_diff * rating_jj_diff
                movie_ii_rating_diff_sqrd += rating_ii_diff ** 2
                movie_jj_rating_diff_sqrd += rating_jj_diff ** 2
            similarity_score = (
                    numerator/(math.sqrt(movie_ii_rating_diff_sqrd) *
                               math.sqrt(movie_jj_rating_diff_sqrd)))
            similarity_cache_dict[(movie_ii, movie_jj)] = similarity_score
            similarity_scores.append((similarity_score,
                                      user_rating_map_jj))
        sorted_similarity_scores = sorted(similarity_scores,
                                          key=lambda x: x[0])
        start_slice_idx = max(0, len(sorted_similarity_scores) - k)
        similarity_map[movie_ii] = sorted_similarity_scores[start_slice_idx:]
    return similarity_map

def predict_adjusted_cosine(train_user_data, similarity_map,
                            user_average_dict, train_movie_data,
                            movie_average_rating_dict):
    score_distribution = defaultdict(int)
    used_average = 0
    for user, (_, unknown_ratings) in train_user_data.items():
        for movie in unknown_ratings.keys():
            similarity_scores = similarity_map.get(movie, [])
            numerator = 0
            denominator = 0
            for similarity_score, user_rating_map in similarity_scores:
                if user not in user_rating_map:
                    continue
                numerator += similarity_score * user_rating_map[user]
                denominator += similarity_score

            if denominator == 0:
                movie_average_rating = movie_average_rating_dict.get(movie, 3)
                prediction = int(movie_average_rating)
                used_average += 1
            else:
                prediction = int(numerator/denominator)
            score_distribution[prediction] += 1
            unknown_ratings[movie] = prediction
    print("Score distribution: %s" % score_distribution)
    print("Used average: %s" % used_average)

def run_against_test(test_file_path, train_user_data, user_average_rating_dict,
                     train_movie_data, output_file_path, prediction_method,
                     iuf_movie_dict):
    test_user_data = read_test(test_file_path, train_movie_data,
                               train_user_data, user_average_rating_dict,
                               iuf_movie_dict)
    movie_average_rating_dict = {}
    for movie, user_rating_map in train_movie_data.items():
        movie_rating_sum = sum(user_rating_map.values())
        movie_average_rating_dict[movie] = (
                float(movie_rating_sum)/len(user_rating_map))

    """
    movies_without_ratings = 0
    movie_rating_distribution = defaultdict(int)
    for ii in range(1, 1001):
        if ii not in train_movie_data:
            print("Movie not rated: %s" % ii)
            movies_without_ratings += 1
        num_ratings = len(train_movie_data[ii])
        movie_rating_distribution[num_ratings] += 1
    print("Total movies not rated: %s" % movies_without_ratings)
    for num_ratings in sorted(movie_rating_distribution.keys()):
        num_movies = movie_rating_distribution[num_ratings]
        print("Number of movies rated %s times: %s" % (num_ratings, num_movies))
    """

    if prediction_method == "cosine":
        similarity_map = similar_users_cosine(train_user_data, test_user_data,
                                              user_average_rating_dict,
                                              movie_average_rating_dict, 200)
        predict_cosine(similarity_map, test_user_data, train_user_data,
                       user_average_rating_dict, movie_average_rating_dict)
    elif prediction_method == "pearson":
        similarity_map = similar_users_pearson(train_user_data, test_user_data,
                                               user_average_rating_dict,
                                               movie_average_rating_dict, 200)
        predict_pearson(similarity_map, test_user_data,
                        movie_average_rating_dict)
    elif prediction_method == "adjusted_cosine":
        similarity_map = similarity_movies_adjusted_cosine(
                train_movie_data,
                user_average_rating_dict,
                200)
        predict_adjusted_cosine(test_user_data, similarity_map,
                                user_average_rating_dict, train_movie_data,
                                movie_average_rating_dict)
    else:
        print("Invalid prediction method %s, the supported methods "
              "are: cosine, pearson, adjusted_cosine" % prediction_method)
        sys.exit(-1)

    with open(output_file_path, 'w') as f:
        for user, (_, unknown_ratings) in test_user_data.items():
            for movie, rating in unknown_ratings.items():
                f.write("%d %d %d\n" % (user, movie, rating))
                if rating < 1 or rating > 5:
                    print("Invalid rating: %s" % rating)

if __name__ == "__main__":
    if len(sys.argv) != 5:
        print("Usage: python <path to script> <train data path> "
              "<test data path> <prediction method> <output path>")
        sys.exit(-1)

    train_data_path = sys.argv[1]
    test_data_path = sys.argv[2]
    prediction_method = sys.argv[3]
    output_path = sys.argv[4]

    (train_user_data, user_average_rating_dict,
     train_movie_data, iuf_movie_dict) = read_train(train_data_path)
    run_against_test(test_data_path, train_user_data, user_average_rating_dict,
                     train_movie_data, output_path, prediction_method,
                     iuf_movie_dict)
