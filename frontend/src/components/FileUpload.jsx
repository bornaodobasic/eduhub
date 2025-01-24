import React, { useState } from "react";

const FileUpload = () => {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!file) {
            setMessage("Please select a file first!");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch("https://eduhub-rfsg.onrender.com", {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                const data = await response.text();
                setMessage("File uploaded successfully: " + data);
            } else {
                setMessage("Error uploading file: " + response.statusText);
            }
        } catch (error) {
            setMessage("Error: " + error.message);
        }
    };

    return (
        <div>
            <h1>Upload a File</h1>
            <form onSubmit={handleSubmit}>
                <input type="file" onChange={handleFileChange} />
                <button type="submit">Upload</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default FileUpload;
