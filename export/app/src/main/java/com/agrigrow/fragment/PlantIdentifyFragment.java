package com.agrigrow.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.agrigrow.R;
import com.agrigrow.util.PlantIdentificationHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment for plant identification functionality.
 * Allows users to take photos or select images from gallery to identify plants.
 */
public class PlantIdentifyFragment extends Fragment {

    private static final String TAG = "PlantIdentifyFragment";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private ImageView imageViewPreview;
    private Button buttonTakePhoto;
    private Button buttonSelectImage;
    private Button buttonIdentify;
    private FrameLayout loadingLayout;

    private Uri currentPhotoUri;
    private Bitmap currentImageBitmap;
    private PlantIdentificationHelper plantIdHelper;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    public PlantIdentifyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plantIdHelper = PlantIdentificationHelper.getInstance(requireContext());
        
        // Initialize permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = true;
                    for (Boolean granted : permissions.values()) {
                        if (!granted) {
                            allGranted = false;
                            break;
                        }
                    }
                    
                    if (allGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(getContext(), 
                                "Camera and storage permissions are required", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        
        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            if (currentPhotoUri != null) {
                                processImageUri(currentPhotoUri);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), 
                                    "Failed to load image: " + e.getMessage(), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        
        // Initialize gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            processImageUri(selectedImageUri);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_identify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        imageViewPreview = view.findViewById(R.id.imageViewPreview);
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        buttonIdentify = view.findViewById(R.id.buttonIdentify);
        loadingLayout = view.findViewById(R.id.loadingLayout);

        // Set click listeners
        buttonTakePhoto.setOnClickListener(v -> {
            if (checkAndRequestCameraPermissions()) {
                openCamera();
            }
        });

        buttonSelectImage.setOnClickListener(v -> {
            if (checkAndRequestStoragePermissions()) {
                openGallery();
            }
        });

        buttonIdentify.setOnClickListener(v -> {
            if (currentPhotoUri != null || currentImageBitmap != null) {
                identifyPlant();
            } else {
                Toast.makeText(getContext(), "Please take or select an image first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkAndRequestCameraPermissions() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                });
                return false;
            }
        }
        return false;
    }

    private boolean checkAndRequestStoragePermissions() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                });
                return false;
            }
        }
        return false;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the file where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error creating image file", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                cameraLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(getContext(), "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(null);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void processImageUri(Uri imageUri) {
        try {
            currentPhotoUri = imageUri;
            currentImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            imageViewPreview.setImageBitmap(currentImageBitmap);
            buttonIdentify.setEnabled(true);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    private void identifyPlant() {
        if (currentPhotoUri == null && currentImageBitmap == null) {
            Toast.makeText(getContext(), "Please take or select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        loadingLayout.setVisibility(View.VISIBLE);

        // Identify plant
        if (currentPhotoUri != null) {
            plantIdHelper.identifyPlantFromUri(currentPhotoUri, new PlantIdentificationHelper.OnPlantIdentificationListener() {
                @Override
                public void onIdentificationResults(List<PlantIdentificationHelper.PlantIdentificationResult> results) {
                    loadingLayout.setVisibility(View.GONE);
                    if (results != null && !results.isEmpty()) {
                        navigateToResultsFragment(results, currentPhotoUri);
                    } else {
                        Toast.makeText(getContext(), "No plants identified", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onIdentificationError(String errorMessage) {
                    loadingLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (currentImageBitmap != null) {
            plantIdHelper.identifyPlantFromBitmap(currentImageBitmap, new PlantIdentificationHelper.OnPlantIdentificationListener() {
                @Override
                public void onIdentificationResults(List<PlantIdentificationHelper.PlantIdentificationResult> results) {
                    loadingLayout.setVisibility(View.GONE);
                    if (results != null && !results.isEmpty()) {
                        navigateToResultsFragment(results, null);
                    } else {
                        Toast.makeText(getContext(), "No plants identified", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onIdentificationError(String errorMessage) {
                    loadingLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToResultsFragment(List<PlantIdentificationHelper.PlantIdentificationResult> results, Uri imageUri) {
        if (getActivity() != null) {
            PlantResultFragment resultFragment = new PlantResultFragment();
            
            // Create bundle with results
            Bundle args = new Bundle();
            args.putParcelable("PLANT_IMAGE_URI", imageUri);
            
            // We can only pass simple data through a bundle, so we'll store the results and retrieve them
            PlantResultFragment.setLastIdentificationResults(results);
            
            resultFragment.setArguments(args);
            
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, resultFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}