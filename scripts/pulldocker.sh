IMAGE_NAME=$(head -n 1 imagename.txt)
echo ${IMAGE_NAME}
sudo docker pull ${IMAGE_NAME}
