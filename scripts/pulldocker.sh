IMAGE_NAME=$(head -n 1 /home/ubuntu/artifacts/imagename.txt)
echo ${IMAGE_NAME}
sudo docker pull ${IMAGE_NAME}
