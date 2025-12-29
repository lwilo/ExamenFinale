from datetime import datetime, timedelta
import random

AUTHOR_NAME = b"Saad Lili"
AUTHOR_EMAIL = b"saadlili716@gmail.com"

START_HOUR = 14
END_HOUR = 17
MIN_GAP_MINUTES = 12

last_time = None

def commit_callback(commit):
    global last_time

    # Author & Committer
    commit.author_name = AUTHOR_NAME
    commit.author_email = AUTHOR_EMAIL
    commit.committer_name = AUTHOR_NAME
    commit.committer_email = AUTHOR_EMAIL

    base_date = datetime.fromtimestamp(commit.author_date)

    if last_time is None:
        hour = random.randint(START_HOUR, END_HOUR - 1)
        minute = random.randint(0, 59)
        new_time = base_date.replace(hour=hour, minute=minute, second=0)
    else:
        gap = timedelta(minutes=random.randint(MIN_GAP_MINUTES, MIN_GAP_MINUTES + 25))
        new_time = last_time + gap
        if new_time.hour >= END_HOUR:
            new_time = new_time.replace(hour=END_HOUR - 1, minute=55)

    last_time = new_time
    ts = int(new_time.timestamp())

    commit.author_date = ts
    commit.committer_date = ts
