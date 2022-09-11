
def price_is_valid(price_str):
    try:
        price = float(price_str)
        if price < 0:
            return False

        return True
    except ValueError:
        return False


def rating_is_valid(rating_str):
    try:
        rating = float(rating_str)
        if rating < 0 or rating > 5:
            return False

        return True
    except ValueError:
        return False
