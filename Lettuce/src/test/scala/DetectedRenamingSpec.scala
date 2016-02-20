/**
  * Created by S-Shimotori on 2/20/16.
  */

import org.specs2.mutable.Specification

class DetectedRenamingSpec extends Specification {
  "detected-renaming data should" >> {

    "parse valid data successfully" >> {
      val detectedData = DetectedRenaming(
        "/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;"
      ).get
      detectedData.rawData must beEqualTo("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
      detectedData.projectName must beEqualTo("ArgoUML")
      detectedData.directoryPath must beEqualTo("/modules/classfile/src/org/argouml/uml/reveng/classfile/")
      detectedData.fileName must beEqualTo("ByteToken.java")
      detectedData.revisions must beEqualTo(("1.2", "1.3"))
      detectedData.lines must beEqualTo(Some(34, 38))
      detectedData.kinds must beEqualTo((Kind.FD, Kind.FD))
      detectedData.names must beEqualTo(("_val", "val"))
      detectedData.types must beEqualTo(("byte", "byte"))
      detectedData.accessModifiers must beEqualTo(("protected", "private"))
    }

    "parse valid package data successfully" >> {
      val detectedData = DetectedRenaming(
        "/repository/software/FileLevelCommits/ArgoUML/tests/org/argouml/model/_DiffLine_-1.1:,:org.argouml.model->org.argouml.model.uml:PACKD->PACKD"
      ).get
      detectedData.rawData must beEqualTo("/repository/software/FileLevelCommits/ArgoUML/tests/org/argouml/model/_DiffLine_-1.1:,:org.argouml.model->org.argouml.model.uml:PACKD->PACKD")
      detectedData.projectName must beEqualTo("ArgoUML")
      detectedData.directoryPath must beEqualTo("/tests/org/argouml/model/")
      detectedData.fileName must beEqualTo("")
      detectedData.revisions must beEqualTo(("", "1.1"))
      detectedData.lines must beEqualTo(None)
      detectedData.kinds must beEqualTo((Kind.PACKD, Kind.PACKD))
      detectedData.names must beEqualTo(("org.argouml.model", "org.argouml.model.uml"))
      detectedData.types must beEqualTo(("", ""))
      detectedData.accessModifiers must beEqualTo(("", ""))
    }

    "parse unsuccessfully" >> {
      "empty string" >> {
        val detectedData = DetectedRenaming("")
        detectedData must beEqualTo(None)
      }

      "no _DiffLine_" >> {
        val detectedData = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_HogeLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData must beEqualTo(None)
      }

      "no hyphen for revisions" >> {
        val detectedData = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2hoge1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData must beEqualTo(None)
      }

      "no revisions not for package" >> {
        val detectedData0 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_-:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData0 must beEqualTo(None)

        val detectedData1 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData1 must beEqualTo(None)

        val detectedData2 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData2 must beEqualTo(None)
      }

      "invalid path" >> {
        val detectedData0 = DetectedRenaming("/repository/software/FileLevelCommits/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData0 must beEqualTo(None)

        val detectedData1 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData1 must beEqualTo(None)
      }

      "invalid kinds for PACKD data" >> {
        val detectedData0 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/tests/org/argouml/model/_DiffLine_-1.1:,:org.argouml.model->org.argouml.model.uml:HOGE->HOGE")
        detectedData0 must beEqualTo(None)

        val detectedData1 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/tests/org/argouml/model/_DiffLine_-1.1:,:org.argouml.model->org.argouml.model.uml:HOGE->PACKD")
        detectedData1 must beEqualTo(None)

        val detectedData2 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/tests/org/argouml/model/_DiffLine_-1.1:,:org.argouml.model->org.argouml.model.uml:PACKD->HOGE")
        detectedData2 must beEqualTo(None)
      }

      "no arrow for name" >> {
        val detectedData = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/tests/org/argouml/model/_DiffLine_-1.1:,:org.argouml.modelhogeorg.argouml.model.uml:PACKD->PACKD")
        detectedData must beEqualTo(None)
      }

      "no arrow not for PACKD data" >> {
        val detectedData = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;hoge38;FD;val;byte;private;")
        detectedData must beEqualTo(None)
      }

      "invalid identifier data not for PACKD data" >> {
        val detectedData0 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;bytehogeprotected;->38;FD;val;bytehogeprivate;")
        detectedData0 must beEqualTo(None)

        val detectedData1 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;bytehogeprotected;->38;FD;val;byte;private;")
        detectedData1 must beEqualTo(None)

        val detectedData2 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;bytehogeprivate;")
        detectedData2 must beEqualTo(None)

      }

      "no file name not for PACKD data" >> {
        val detectedData = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData must beEqualTo(None)
      }

      "invalid lines not for PACKD data" >> {
        val detectedData0 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:hoge;FD;_val;byte;protected;->hoge;FD;val;byte;private;")
        detectedData0 must beEqualTo(None)

        val detectedData1 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:hoge;FD;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData1 must beEqualTo(None)

        val detectedData2 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->hoge;FD;val;byte;private;")
        detectedData2 must beEqualTo(None)
      }

      "invalid kinds not for PACKD data" >> {
        val detectedData0 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;Hoge;_val;byte;protected;->38;Hoge;val;byte;private;")
        detectedData0 must beEqualTo(None)

        val detectedData1 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;Hoge;_val;byte;protected;->38;FD;val;byte;private;")
        detectedData1 must beEqualTo(None)

        val detectedData2 = DetectedRenaming("/repository/software/FileLevelCommits/ArgoUML/modules/classfile/src/org/argouml/uml/reveng/classfile/ByteToken.java_DiffLine_1.2-1.3:34,38:_val->val:34;FD;_val;byte;protected;->38;Hoge;val;byte;private;")
        detectedData2 must beEqualTo(None)
      }
    }

  }
}
