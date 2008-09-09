package com.android.GLTestApp.DxfRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import com.android.GLTestApp.GLTutorialBase;

import android.graphics.Color;
import android.util.Log;

public class DXFRenderer {
	int dxfFileLineNumber;
	boolean mLoaded;
	ArrayList<Float> mFacesVertex;
	ArrayList<Float> mColorsVertex;
	ArrayList<Float> mNormalsVertex;
	ArrayList<Float> mLinesVertex;
    ArrayList<DXFLayer> mLayers; // DXFLayerList
    ArrayList<DXFEntity> mEntities; // DXFEntityList
    int[][] aci_to_rgb = new int[][] { 
    		/*   0 */ {255, 255, 255},
    		/*   1 */ {255,   0,   0},
    		/*   2 */ {255, 255,   0},
    		/*   3 */ {  0, 255,   0},
    		/*   4 */ {  0, 255, 255},
    		/*   5 */ {  0,   0, 255},
    		/*   6 */ {255,   0, 255},
    		/*   7 */ {255, 255, 255},
    		/*   8 */ {128, 128, 128},
    		/*   9 */ {192, 192, 192},
    		/*  10 */ {255,   0,   0},
    		/*  11 */ {255, 127, 127},
    		/*  12 */ {204,   0,   0},
    		/*  13 */ {204, 102, 102},
    		/*  14 */ {153,   0,   0},
    		/*  15 */ {153,  76,  76},
    		/*  16 */ {127,   0,   0},
    		/*  17 */ {127,  63,  63},
    		/*  18 */ { 76,   0,   0},
    		/*  19 */ { 76,  38,  38},
    		/*  20 */ {255,  63,   0},
    		/*  21 */ {255, 159, 127},
    		/*  22 */ {204,  51,   0},
    		/*  23 */ {204, 127, 102},
    		/*  24 */ {153,  38,   0},
    		/*  25 */ {153,  95,  76},
    		/*  26 */ {127,  31,   0},
    		/*  27 */ {127,  79,  63},
    		/*  28 */ { 76,  19,   0},
    		/*  29 */ { 76,  47,  38},
    		/*  30 */ {255, 127,   0},
    		/*  31 */ {255, 191, 127},
    		/*  32 */ {204, 102,   0},
    		/*  33 */ {204, 153, 102},
    		/*  34 */ {153,  76,   0},
    		/*  35 */ {153, 114,  76},
    		/*  36 */ {127,  63,   0},
    		/*  37 */ {127,  95,  63},
    		/*  38 */ { 76,  38,   0},
    		/*  39 */ { 76,  57,  38},
    		/*  40 */ {255, 191,   0},
    		/*  41 */ {255, 223, 127},
    		/*  42 */ {204, 153,   0},
    		/*  43 */ {204, 178, 102},
    		/*  44 */ {153, 114,   0},
    		/*  45 */ {153, 133,  76},
    		/*  46 */ {127,  95,   0},
    		/*  47 */ {127, 111,  63},
    		/*  48 */ { 76,  57,   0},
    		/*  49 */ { 76,  66,  38},
    		/*  50 */ {255, 255,   0},
    		/*  51 */ {255, 255, 127},
    		/*  52 */ {204, 204,   0},
    		/*  53 */ {204, 204, 102},
    		/*  54 */ {153, 153,   0},
    		/*  55 */ {153, 153,  76},
    		/*  56 */ {127, 127,   0},
    		/*  57 */ {127, 127,  63},
    		/*  58 */ { 76,  76,   0},
    		/*  59 */ { 76,  76,  38},
    		/*  60 */ {191, 255,   0},
    		/*  61 */ {223, 255, 127},
    		/*  62 */ {153, 204,   0},
    		/*  63 */ {178, 204, 102},
    		/*  64 */ {114, 153,   0},
    		/*  65 */ {133, 153,  76},
    		/*  66 */ { 95, 127,   0},
    		/*  67 */ {111, 127,  63},
    		/*  68 */ { 57,  76,   0},
    		/*  69 */ { 66,  76,  38},
    		/*  70 */ {127, 255,   0},
    		/*  71 */ {191, 255, 127},
    		/*  72 */ {102, 204,   0},
    		/*  73 */ {153, 204, 102},
    		/*  74 */ { 76, 153,   0},
    		/*  75 */ {114, 153,  76},
    		/*  76 */ { 63, 127,   0},
    		/*  77 */ { 95, 127,  63},
    		/*  78 */ { 38,  76,   0},
    		/*  79 */ { 57,  76,  38},
    		/*  80 */ { 63, 255,   0},
    		/*  81 */ {159, 255, 127},
    		/*  82 */ { 51, 204,   0},
    		/*  83 */ {127, 204, 102},
    		/*  84 */ { 38, 153,   0},
    		/*  85 */ { 95, 153,  76},
    		/*  86 */ { 31, 127,   0},
    		/*  87 */ { 79, 127,  63},
    		/*  88 */ { 19,  76,   0},
    		/*  89 */ { 47,  76,  38},
    		/*  90 */ {  0, 255,   0},
    		/*  91 */ {127, 255, 127},
    		/*  92 */ {  0, 204,   0},
    		/*  93 */ {102, 204, 102},
    		/*  94 */ {  0, 153,   0},
    		/*  95 */ { 76, 153,  76},
    		/*  96 */ {  0, 127,   0},
    		/*  97 */ { 63, 127,  63},
    		/*  98 */ {  0,  76,   0},
    		/*  99 */ { 38,  76,  38},
    		/* 100 */ {  0, 255,  63},
    		/* 101 */ {127, 255, 159},
    		/* 102 */ {  0, 204,  51},
    		/* 103 */ {102, 204, 127},
    		/* 104 */ {  0, 153,  38},
    		/* 105 */ { 76, 153,  95},
    		/* 106 */ {  0, 127,  31},
    		/* 107 */ { 63, 127,  79},
    		/* 108 */ {  0,  76,  19},
    		/* 109 */ { 38,  76,  47},
    		/* 110 */ {  0, 255, 127},
    		/* 111 */ {127, 255, 191},
    		/* 112 */ {  0, 204, 102},
    		/* 113 */ {102, 204, 153},
    		/* 114 */ {  0, 153,  76},
    		/* 115 */ { 76, 153, 114},
    		/* 116 */ {  0, 127,  63},
    		/* 117 */ { 63, 127,  95},
    		/* 118 */ {  0,  76,  38},
    		/* 119 */ { 38,  76,  57},
    		/* 120 */ {  0, 255, 191},
    		/* 121 */ {127, 255, 223},
    		/* 122 */ {  0, 204, 153},
    		/* 123 */ {102, 204, 178},
    		/* 124 */ {  0, 153, 114},
    		/* 125 */ { 76, 153, 133},
    		/* 126 */ {  0, 127,  95},
    		/* 127 */ { 63, 127, 111},
    		/* 128 */ {  0,  76,  57},
    		/* 129 */ { 38,  76,  66},
    		/* 130 */ {  0, 255, 255},
    		/* 131 */ {127, 255, 255},
    		/* 132 */ {  0, 204, 204},
    		/* 133 */ {102, 204, 204},
    		/* 134 */ {  0, 153, 153},
    		/* 135 */ { 76, 153, 153},
    		/* 136 */ {  0, 127, 127},
    		/* 137 */ { 63, 127, 127},
    		/* 138 */ {  0,  76,  76},
    		/* 139 */ { 38,  76,  76},
    		/* 140 */ {  0, 191, 255},
    		/* 141 */ {127, 223, 255},
    		/* 142 */ {  0, 153, 204},
    		/* 143 */ {102, 178, 204},
    		/* 144 */ {  0, 114, 153},
    		/* 145 */ { 76, 133, 153},
    		/* 146 */ {  0,  95, 127},
    		/* 147 */ { 63, 111, 127},
    		/* 148 */ {  0,  57,  76},
    		/* 149 */ { 38,  66,  76},
    		/* 150 */ {  0, 127, 255},
    		/* 151 */ {127, 191, 255},
    		/* 152 */ {  0, 102, 204},
    		/* 153 */ {102, 153, 204},
    		/* 154 */ {  0,  76, 153},
    		/* 155 */ { 76, 114, 153},
    		/* 156 */ {  0,  63, 127},
    		/* 157 */ { 63,  95, 127},
    		/* 158 */ {  0,  38,  76},
    		/* 159 */ { 38,  57,  76},
    		/* 160 */ {  0,  63, 255},
    		/* 161 */ {127, 159, 255},
    		/* 162 */ {  0,  51, 204},
    		/* 163 */ {102, 127, 204},
    		/* 164 */ {  0,  38, 153},
    		/* 165 */ { 76,  95, 153},
    		/* 166 */ {  0,  31, 127},
    		/* 167 */ { 63,  79, 127},
    		/* 168 */ {  0,  19,  76},
    		/* 169 */ { 38,  47,  76},
    		/* 170 */ {  0,   0, 255},
    		/* 171 */ {127, 127, 255},
    		/* 172 */ {  0,   0, 204},
    		/* 173 */ {102, 102, 204},
    		/* 174 */ {  0,   0, 153},
    		/* 175 */ { 76,  76, 153},
    		/* 176 */ {  0,   0, 127},
    		/* 177 */ { 63,  63, 127},
    		/* 178 */ {  0,   0,  76},
    		/* 179 */ { 38,  38,  76},
    		/* 180 */ { 63,   0, 255},
    		/* 181 */ {159, 127, 255},
    		/* 182 */ { 51,   0, 204},
    		/* 183 */ {127, 102, 204},
    		/* 184 */ { 38,   0, 153},
    		/* 185 */ { 95,  76, 153},
    		/* 186 */ { 31,   0, 127},
    		/* 187 */ { 79,  63, 127},
    		/* 188 */ { 19,   0,  76},
    		/* 189 */ { 47,  38,  76},
    		/* 190 */ {127,   0, 255},
    		/* 191 */ {191, 127, 255},
    		/* 192 */ {102,   0, 204},
    		/* 193 */ {153, 102, 204},
    		/* 194 */ { 76,   0, 153},
    		/* 195 */ {114,  76, 153},
    		/* 196 */ { 63,   0, 127},
    		/* 197 */ { 95,  63, 127},
    		/* 198 */ { 38,   0,  76},
    		/* 199 */ { 57,  38,  76},
    		/* 200 */ {191,   0, 255},
    		/* 201 */ {223, 127, 255},
    		/* 202 */ {153,   0, 204},
    		/* 203 */ {178, 102, 204},
    		/* 204 */ {114,   0, 153},
    		/* 205 */ {133,  76, 153},
    		/* 206 */ { 95,   0, 127},
    		/* 207 */ {111,  63, 127},
    		/* 208 */ { 57,   0,  76},
    		/* 209 */ { 66,  38,  76},
    		/* 210 */ {255,   0, 255},
    		/* 211 */ {255, 127, 255},
    		/* 212 */ {204,   0, 204},
    		/* 213 */ {204, 102, 204},
    		/* 214 */ {153,   0, 153},
    		/* 215 */ {153,  76, 153},
    		/* 216 */ {127,   0, 127},
    		/* 217 */ {127,  63, 127},
    		/* 218 */ { 76,   0,  76},
    		/* 219 */ { 76,  38,  76},
    		/* 220 */ {255,   0, 191},
    		/* 221 */ {255, 127, 223},
    		/* 222 */ {204,   0, 153},
    		/* 223 */ {204, 102, 178},
    		/* 224 */ {153,   0, 114},
    		/* 225 */ {153,  76, 133},
    		/* 226 */ {127,   0,  95},
    		/* 227 */ {127,  63, 111},
    		/* 228 */ { 76,   0,  57},
    		/* 229 */ { 76,  38,  66},
    		/* 230 */ {255,   0, 127},
    		/* 231 */ {255, 127, 191},
    		/* 232 */ {204,   0, 102},
    		/* 233 */ {204, 102, 153},
    		/* 234 */ {153,   0,  76},
    		/* 235 */ {153,  76, 114},
    		/* 236 */ {127,   0,  63},
    		/* 237 */ {127,  63,  95},
    		/* 238 */ { 76,   0,  38},
    		/* 239 */ { 76,  38,  57},
    		/* 240 */ {255,   0,  63},
    		/* 241 */ {255, 127, 159},
    		/* 242 */ {204,   0,  51},
    		/* 243 */ {204, 102, 127},
    		/* 244 */ {153,   0,  38},
    		/* 245 */ {153,  76,  95},
    		/* 246 */ {127,   0,  31},
    		/* 247 */ {127,  63,  79},
    		/* 248 */ { 76,   0,  19},
    		/* 249 */ { 76,  38,  47},
    		/* 250 */ { 51,  51,  51},
    		/* 251 */ { 91,  91,  91},
    		/* 252 */ {132, 132, 132},
    		/* 253 */ {173, 173, 173},
    		/* 254 */ {214, 214, 214},
    		/* 255 */ {255, 255, 255}
    		};

	public DXFRenderer() {
		this.mLoaded = false;
		this.mEntities = new ArrayList<DXFEntity>();
		this.mLayers = new ArrayList<DXFLayer>();
		this.mFacesVertex = new ArrayList<Float>();
		this.mColorsVertex = new ArrayList<Float>();
		this.mNormalsVertex = new ArrayList<Float>();
		this.mLinesVertex = new ArrayList<Float>();
		this.dxfFileLineNumber = 0;
	}
	
	public static DXFVector Cross(DXFVector v1, DXFVector v2) {
	    return new DXFVector(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x);
	}
	
	int GetLayerColour(String layer) {
	    for (DXFLayer current : mLayers) 
	        if (current.name == layer)
	            return current.colour;
	    
	    return 7;   // white
	}
	
	String GetLines(BufferedReader text)
	{
		String test = null;
	    try {
	    	test = text.readLine().trim();
	    	this.dxfFileLineNumber++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return test;

	}
	
	// parse header section: just skip everything
	boolean ParseHeader(InputStreamReader stream, BufferedReader text) {
	    String line1 = null, line2 = null;
	    for (line1 = GetLines(text), line2 = GetLines(text); line2.compareTo("EOF") != 0; line1 = GetLines(text), line2 = GetLines(text)) {
		    //line1 = GetLines(text);
		    //line2 = GetLines(text);
		    if ((line1.compareTo("0") == 0) && (line2.compareTo("ENDSEC") == 0))
		        return true;
		}
	    return false;
	}
	
	// parse tables section: save layers name and colour
	boolean ParseTables(InputStreamReader stream, BufferedReader text)
	{
	    String line1 = null, line2 = null;
	    boolean inlayer = false;
	    DXFLayer layer = new DXFLayer();
	    try {
		    for (line1 = GetLines(text), line2 = GetLines(text); line2.compareTo("EOF") != 0; line1 = GetLines(text), line2 = GetLines(text)) {
			    //line1 = GetLines(text);
			    //line2 = GetLines(text);
			    if ((line1.compareTo("0") == 0) && inlayer) {
			        // flush layer
			        if ((layer.name != null) && (layer.colour != -1)) {
			            DXFLayer p = new DXFLayer();
			            p.name = layer.name;
			            p.colour = layer.colour;
			            mLayers.add(p);
			        }
			        layer = new DXFLayer();
			        inlayer = false;
			    }
			    if ((line1.compareTo("0") == 0) && (line2.compareTo("ENDSEC") == 0))
			        return true;
			    else if ((line1.compareTo("0") == 0) && (line2.compareTo("LAYER") == 0))
			        inlayer = true;
			    else if (inlayer) {
			        if (line1.compareTo("2") == 0) // layer name
			            layer.name = line2;
			        else if (line1.compareTo("62") == 0) { // ACI colour
			        	layer.colour = (int) Long.parseLong(line2);
			        }
			    }
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	    return false;
	}
	
	// parse entities section: save 3DFACE and LINE entities
	boolean ParseEntities(InputStreamReader stream, BufferedReader text) {
	    String line1 = null, line2 = null;
	    int state = 0;  // 0: none, 1: 3DFACE, 2: LINE
	    DXFVector v[] = new DXFVector[] { 
	    		new DXFVector(),
	    		new DXFVector(),
	    		new DXFVector(),
	    		new DXFVector()
	    };
	    int colour = -1;
	    String layer = null;
	    try {
		    for (line1 = GetLines(text), line2 = GetLines(text); line2.compareTo("EOF") != 0; line1 = GetLines(text), line2 = GetLines(text)) {
			    //line1 = GetLines(text);
			    //line2 = GetLines(text);
			    if ((line1.compareTo("0") == 0) && state > 0) {
			        // flush entity
			        if (state == 1) { // 3DFACE
			            DXFFace p = new DXFFace();
			            p.v0 = new DXFVector(v[0].x, v[0].y, v[0].z);
			            p.v1 = new DXFVector(v[1].x, v[1].y, v[1].z);
			            p.v2 = new DXFVector(v[2].x, v[2].y, v[2].z);
			            p.v3 = new DXFVector(v[3].x, v[3].y, v[3].z);
			            p.CalculateNormal();
			            if (colour != -1)
			                p.colour = colour;
			            else
			                p.colour = GetLayerColour(layer);
			            mEntities.add(p);
			            colour = -1; 
			            layer = null;
			            for (int i = 0; i < v.length; i++)
			            	v[i] = new DXFVector();
			            state = 0;
			        }
			        else if (state == 2) { // LINE
			            DXFLine p = new DXFLine();
			            p.v0 = new DXFVector(v[0].x, v[0].y, v[0].z);
			            p.v1 = new DXFVector(v[1].x, v[1].y, v[1].z);
			            if (colour != -1)
			                p.colour = colour;
			            else
			                p.colour = GetLayerColour(layer);
			            mEntities.add(p);
			            colour = -1; 
			            layer = null;
			            for (int i = 0; i < v.length; i++)
			            	v[i] = new DXFVector();
			            state = 0;
			        }
			    }
			    if ((line1.compareTo("0") == 0) && (line2.compareTo("ENDSEC") == 0))
 			        return true;
			    else if ((line1.compareTo("0") == 0) && (line2.compareTo("3DFACE") == 0))
			        state = 1;
			    else if ((line1.compareTo("0") == 0) && (line2.compareTo("LINE") == 0))
			        state = 2;
			    else if (state > 0) {
			        double d = Double.parseDouble(line2);
			        if (line1.compareTo("10") == 0)
			            v[0].x = (float) d;
			        else if (line1.compareTo("20") == 0)
			            v[0].y = (float) d;
			        else if (line1.compareTo("30") == 0)
			            v[0].z = (float) d;
			        else if (line1.compareTo("11") == 0)
			            v[1].x = (float) d;
			        else if (line1.compareTo("21") == 0)
			            v[1].y = (float) d;
			        else if (line1.compareTo("31") == 0)
			            v[1].z = (float) d;
			        else if (line1.compareTo("12") == 0)
			            v[2].x = (float) d;
			        else if (line1.compareTo("22") == 0)
			            v[2].y = (float) d;
			        else if (line1.compareTo("32") == 0)
			            v[2].z = (float) d;
			        else if (line1.compareTo("13") == 0)
			            v[3].x = (float) d;
			        else if (line1.compareTo("23") == 0)
			            v[3].y = (float) d;
			        else if (line1.compareTo("33") == 0)
			            v[3].z = (float) d;
			        else if (line1.compareTo("8") == 0)  // layer
			            layer = line2;
			        else if (line1.compareTo("62") == 0) // colour
			            colour = (int) Long.parseLong(line2);
			    }
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	    return false;
	}

	// deallocate all the dynamic data
	void Clear() {
	    mLoaded = false;
	    
	    mLayers.clear();
	    mEntities.clear();	    
	}

	
	// parse and load a DXF file
	// currently pretty limited, but knows enought do handle 3DFACEs and LINEs
	public boolean Load(InputStreamReader stream) {
	    Clear();

	    BufferedReader text = new BufferedReader(stream);

	    String line1 = null, line2 = null;
	    for (line1 = GetLines(text), line2 = GetLines(text); line2.compareTo("EOF") != 0; line1 = GetLines(text), line2 = GetLines(text)) {
		    //line1 = GetLines(text);
		    //line2 = GetLines(text);
		    if (line1.compareTo("999") == 0) // comment
		        continue;
		    else if ((line1.compareTo("0") == 0) && (line2.compareTo("SECTION") == 0)) {
			    line1 = GetLines(text);
			    line2 = GetLines(text);
		        if (line1.compareTo("2") == 0)
		        {
		            if (line2.compareTo("HEADER") == 0)
		            {
		                if (!ParseHeader(stream, text))
		                    return false;
		            }
		            else if (line2.compareTo("TABLES") == 0)
		            {
		                if (!ParseTables(stream, text))
		                    return false;
		            }
		            else if (line2.compareTo("ENTITIES") == 0)
		            {
		                if (!ParseEntities(stream, text))
		                    return false;
		            }
		        }
		    }
		}

	    NormalizeEntities();
	    mLoaded = true;
	    return true;
	}
	
	float mymin(float x, float y) { return x < y ? x : y; }
	float mymax(float x, float y) { return x > y ? x : y; }

	// Scale object boundings to [-5,5]
	void NormalizeEntities() {
	    // calculate current min and max boundings of object
	    DXFVector minv = new DXFVector(10e20f, 10e20f, 10e20f);
	    DXFVector maxv = new DXFVector(-10e20f, -10e20f, -10e20f);
	    for (DXFEntity p : mEntities) {
	        if (p.type == DXFEntityType.Line) {
	            DXFLine line = (DXFLine) p;
	            DXFVector v[] = new DXFVector[] { line.v0, line.v1 };
	            for (int i = 0; i < 2; i++) {
	                minv.x = mymin(v[i].x, minv.x);
	                minv.y = mymin(v[i].y, minv.y);
	                minv.z = mymin(v[i].z, minv.z);
	                maxv.x = mymax(v[i].x, maxv.x);
	                maxv.y = mymax(v[i].y, maxv.y);
	                maxv.z = mymax(v[i].z, maxv.z);
	            }
	        } else if (p.type == DXFEntityType.Face) {
	            DXFFace face = (DXFFace) p;
	            DXFVector v[] = new DXFVector[] { face.v0, face.v1, face.v2, face.v3 };
	            for (int i = 0; i < 4; i++) {
	                minv.x = mymin(v[i].x, minv.x);
	                minv.y = mymin(v[i].y, minv.y);
	                minv.z = mymin(v[i].z, minv.z);
	                maxv.x = mymax(v[i].x, maxv.x);
	                maxv.y = mymax(v[i].y, maxv.y);
	                maxv.z = mymax(v[i].z, maxv.z);
	            }
	       }
	    }

	    // rescale object down to [-5,5]
	    DXFVector span = new DXFVector(maxv.x - minv.x, maxv.y - minv.y, maxv.z - minv.z);
	    float factor = mymin(mymin(10.0f / span.x, 10.0f / span.y), 10.0f / span.z);
	    for (DXFEntity p : mEntities) {
	        if (p.type == DXFEntityType.Line) {
	            DXFLine line = (DXFLine) p;
	            DXFVector v[] = new DXFVector[] { line.v0, line.v1 };
	            for (int i = 0; i < 2; i++)
	            {
	                v[i].x -= minv.x + span.x/2; v[i].x *= factor;
	                v[i].y -= minv.y + span.y/2; v[i].y *= factor;
	                v[i].z -= minv.z + span.z/2; v[i].z *= factor;
	            }
	        } else if (p.type == DXFEntityType.Face) {
	            DXFFace face = (DXFFace) p;
	            DXFVector v[] = new DXFVector[] { face.v0, face.v1, face.v2, face.v3 };
	            for (int i = 0; i < 4; i++) {
	                v[i].x -= minv.x + span.x/2; v[i].x *= factor;
	                v[i].y -= minv.y + span.y/2; v[i].y *= factor;
	                v[i].z -= minv.z + span.z/2; v[i].z *= factor;
	            }
	       }
	    }
	}
	
	// OpenGL renderer for DXF entities
	public void RenderToBuffer() {
        int i = 0;
	    if (!mLoaded)
	        return;
	    for (DXFEntity p : mEntities)
	    {
	        int c = ACIColourToRGB(p.colour);
	        if (p.type == DXFEntityType.Line)
	        {
	            DXFLine line = (DXFLine) p;
//	            gl.glColor4f(Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f);
	            mColorsVertex.add(Color.red(c)/255.0f);
	            mColorsVertex.add(Color.green(c)/255.0f);
	            mColorsVertex.add(Color.blue(c)/255.0f);
	            mColorsVertex.add(1.0f);
	            mColorsVertex.add(Color.red(c)/255.0f);
	            mColorsVertex.add(Color.green(c)/255.0f);
	            mColorsVertex.add(Color.blue(c)/255.0f);
	            mColorsVertex.add(1.0f);
	                      
//		        FloatBuffer fb = GLTutorialBase.makeFloatBuffer(
//		        		new float[] { 
//		        				line.v0.x, line.v0.y, line.v0.z,
//		        				line.v1.x, line.v1.y, line.v1.z});
		        mLinesVertex.add(line.v0.x);
		        mLinesVertex.add(line.v0.y);
		        mLinesVertex.add(line.v0.z);
		        mLinesVertex.add(line.v1.x);
		        mLinesVertex.add(line.v1.y);
		        mLinesVertex.add(line.v1.z);
		        Log.i("Line " + i++, "(x0,y0,z0)=(" + line.v0.x + "," + line.v0.y + "," + line.v0.z + ") - (x1,y1,z1)=("+ line.v1.x + "," + line.v1.y + "," + line.v1.z + ")");
//		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
//		        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		        gl.glDrawArrays(GL10.GL_LINES, 0, 2);				        
	        }
	        else if (p.type == DXFEntityType.Face)
	        {
	            DXFFace face = (DXFFace) p;
//	            gl.glColor4f(Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f);
	            mColorsVertex.add(Color.red(c)/255.0f);
	            mColorsVertex.add(Color.green(c)/255.0f);
	            mColorsVertex.add(Color.blue(c)/255.0f);
	            mColorsVertex.add(1.0f);
	            mColorsVertex.add(Color.red(c)/255.0f);
	            mColorsVertex.add(Color.green(c)/255.0f);
	            mColorsVertex.add(Color.blue(c)/255.0f);
	            mColorsVertex.add(1.0f);
	            mColorsVertex.add(Color.red(c)/255.0f);
	            mColorsVertex.add(Color.green(c)/255.0f);
	            mColorsVertex.add(Color.blue(c)/255.0f);
	            mColorsVertex.add(1.0f);
	            
//	            gl.glNormal3f(face.n.x, face.n.y, face.n.z);
	            mNormalsVertex.add(face.n.x);
	            mNormalsVertex.add(face.n.y);
	            mNormalsVertex.add(face.n.z);
	            mNormalsVertex.add(face.n.x);
	            mNormalsVertex.add(face.n.y);
	            mNormalsVertex.add(face.n.z);
	            mNormalsVertex.add(face.n.x);
	            mNormalsVertex.add(face.n.y);
	            mNormalsVertex.add(face.n.z);
	            
//		        FloatBuffer fb = GLTutorialBase.makeFloatBuffer(
//		        		new float[] { 
//		        				face.v0.x, face.v0.y, face.v0.z,
//		        				face.v1.x, face.v1.y, face.v1.z,
//		        				face.v2.x, face.v2.y, face.v2.z});
		        mFacesVertex.add(face.v0.x);
		        mFacesVertex.add(face.v0.y);
		        mFacesVertex.add(face.v0.z);
		        mFacesVertex.add(face.v1.x);
		        mFacesVertex.add(face.v1.y);
		        mFacesVertex.add(face.v1.z);
		        mFacesVertex.add(face.v2.x);
		        mFacesVertex.add(face.v2.y);
		        mFacesVertex.add(face.v2.z);
//		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
//		        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	        }
	    }
	}
	
	// OpenGL renderer for DXF entities
	public void Render(GL10 gl) {
	    if (!mLoaded)
	        return;
	    for (DXFEntity p : mEntities)
	    {
	        int c = ACIColourToRGB(p.colour);
	        if (p.type == DXFEntityType.Line)
	        {
	            DXFLine line = (DXFLine) p;
	            gl.glColor4f(Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f);
	                      
		        FloatBuffer fb = GLTutorialBase.makeFloatBuffer(
		        		new float[] { 
		        				line.v0.x, line.v0.y, line.v0.z,
		        				line.v1.x, line.v1.y, line.v1.z});
		        
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
		        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
		
	        }
	        else if (p.type == DXFEntityType.Face)
	        {
	            DXFFace face = (DXFFace) p;
	            gl.glColor4f(Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f);
	            
//	            FloatBuffer colorFb = GLTutorialBase.makeFloatBuffer(
//	            		new float[] { 
//	            				Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f 
//	            				,Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f 
//	            				,Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f 
//	            		});
//	            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorFb);
//	            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { Color.red(c)/255.0f, Color.green(c)/255.0f, Color.blue(c)/255.0f, 1.0f }, 0);
//	            gl.glEnable(GL10.GL_COLOR_MATERIAL);

	            gl.glNormal3f(face.n.x, face.n.y, face.n.z);
	            
		        FloatBuffer fb = GLTutorialBase.makeFloatBuffer(
		        		new float[] { 
		        				face.v0.x, face.v0.y, face.v0.z,
		        				face.v1.x, face.v1.y, face.v1.z,
		        				face.v2.x, face.v2.y, face.v2.z});
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
		        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	        }
	    }
	}

	
	// convert an AutoCAD ACI colour to wxWidgets RGB colour
	int ACIColourToRGB(int col) {
	      return Color.rgb(aci_to_rgb[col][0], aci_to_rgb[col][1], aci_to_rgb[col][2]);	    
	}
	
	public float[] getFaceArray() {
		float[] f = new float[mFacesVertex.size()];
		for (int i = 0; i < f.length; i++)
			f[i] = (float) mFacesVertex.get(i);
		
		Log.i("FaceArray", "Length=" + f.length);
		return f;
	}

	public float[] getLineArray() {
		float[] f = new float[mLinesVertex.size()];
		for (int i = 0; i < f.length; i++)
			f[i] = (float) mLinesVertex.get(i);
		
		Log.i("LineArray", "Length=" + f.length);
		return f;
	}

	public float[] getColorArray() {
		float[] f = new float[mColorsVertex.size()];
		for (int i = 0; i < f.length; i++)
			f[i] = (float) mColorsVertex.get(i);
		
		Log.i("ColorArray", "Length=" + f.length);
		return f;
	}

	public float[] getNormalArray() {
		float[] f = new float[mNormalsVertex.size()];
		for (int i = 0; i < f.length; i++)
			f[i] = (float) mNormalsVertex.get(i);
		
		Log.i("NormalArray", "Length=" + f.length);
		return f;
	}
	

}
