package libraries

import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import plugins.S3Plugin

import java.util.UUID

object ImageUploader {
  def upload(image: FilePart[TemporaryFile]): Option[String] = {
    val fileName = UUID.randomUUID.toString
    val putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, fileName, image.ref.file);
    putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);

    // Detect the mimetype from the uploaded file and set the metadata so S3
    // will serve it correctly
    val mimeType = image.contentType
    val accetedMimeTypes = List(
      "image/png",
      "image/jpeg",
      "image/gif"
    )
    mimeType match {
      case Some(mimeType) if accetedMimeTypes.contains(mimeType) => {
        val objectMetadata = new ObjectMetadata
        objectMetadata.setContentType(mimeType)
        putObjectRequest.setMetadata(objectMetadata)
        S3Plugin.amazonS3.putObject(putObjectRequest);
        Some(fileName)
      }
      case _ => None
    }
  }
}
