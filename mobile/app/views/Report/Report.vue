<template>
    <Page>
        <ActionBar class="action-bar" title="Beceri Karnesi">
            <ActionItem text="Paylaş" @tap="sharePDF" ios.position="right"/>
        </ActionBar>
        <GridLayout>
            <PDFView :src="pdfFilePath" stretch="aspectFit" />
        </GridLayout>
    </Page>
</template>

<script>
import { nextTick } from "vue";
import * as fs from "@nativescript/core/file-system";
import { Buffer } from 'buffer';
import * as TextModule from '@nativescript/core/text';
import { ShareFile } from 'nativescript-share-file';

export default {
    data() {
        return {
            pdfFilePath: ""
        };
    },
    props: ['studentId', 'bolumId'],
    mounted() {
        this.fetchAndSavePDF();
    },
    methods: {
        async fetchAndSavePDF() {
            const url = `http://${this.$store.state.user.hostIp}:8082/nursapp/app/report/download?studentId=${this.studentId}&bolumId=${this.bolumId}`;
            try {
                const response = await fetch(url, {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + this.$store.state.user.token
                    }
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }

                const base64 = await response.text();
                console.log("Received Base64 Length:", base64.length);

                const binaryString = Buffer.from(base64, 'base64').toString('binary');

                const tempFolderPath = fs.knownFolders.temp().path;
                const pdfFileName = `${this.studentId} - Beceri karnesi.pdf`;
                this.pdfFilePath = fs.path.join(tempFolderPath, pdfFileName);

                const pdfFile = fs.File.fromPath(this.pdfFilePath);
                pdfFile.writeTextSync(binaryString, err => {
                    if (err) {
                        console.error("Error saving file:", err);
                    }
                }, TextModule.encoding.ISO_8859_1);

                nextTick(() => {
                    this.$refs.pdfViewer.src = this.pdfFilePath;
                    console.log("PDF displayed from:", this.pdfFilePath);
                });

            } catch (error) {
                console.error('Error downloading or saving the PDF:', error);
            }
        },
        sharePDF() {
            const shareFile = new ShareFile();
            const pdfFilePath = this.pdfFilePath;
            const options = {
                path: pdfFilePath,
                title: "Share PDF",
                message: "Sharing PDF",
                android: {
                    intentTitle: "Share PDF via"
                }
            };
            try {
                shareFile.open(options);
                console.log("Share successful!");
            } catch (error) {
                console.error("Error sharing file:", error);
            }
        }
    }
};
</script>

<style scoped>
.ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
}
</style>
