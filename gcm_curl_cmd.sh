api_key=AIzaSyBtis6tM-ioXah-jSUkSplUU_78VEEWF4s

reg_id=eU9FCDgpbeY:APA91bHCXROTV4GWZ4ahRZWl9EQuAK8NCJy96EU5ry2LXeKK_H_5vjL-PQg47vWEI9e9kT6CAO6iPWi2k0mmTX8BKUI9FVX21c26-hYpE4yR8emXtkrjE9JknuYK_xavvgMKOPRNn_1I

curl --header "Authorization: key=$api_key" --header Content-Type:"application/json" https://android.googleapis.com/gcm/send  -d "{\"registration_ids\":[\"$reg_id\"], \"data\": { \"message\": \"Hello mobile again\" }}"