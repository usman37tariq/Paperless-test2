ARTIFACTS_DIR=/home/ubuntu/artifacts
IMAGE_NAME=$(head -n 1 ${ARTIFACTS_DIR}/imagename.txt)
echo ${IMAGE_NAME}
sudo docker pull ${IMAGE_NAME}
sudo rm -r ${ARTIFACTS_DIR}/*
