IMAGE_NAME=$(head -n 1 /home/ubuntu/artifacts/imagename.txt)
sudo docker pull $IMAGE_NAME