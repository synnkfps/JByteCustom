package me.synnk.jbytecustom.decompiler;

import com.strobel.assembler.InputTypeLoader;
import com.strobel.assembler.metadata.*;
import com.strobel.decompiler.DecompilationOptions;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.DecompilerPanel;
import me.synnk.jbytecustom.utils.ErrorDisplay;
import org.objectweb.asm.tree.MethodNode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class ProcyonDecompiler extends Decompiler {

    public ProcyonDecompiler(JByteCustom jbm, DecompilerPanel dp) {
        super(jbm, dp);
    }

    public String decompile(byte[] b, MethodNode mn) {
        try {
            //TODO decompile method only
            DecompilerSettings settings = new DecompilerSettings();
            try {
                for (Field f : settings.getClass().getDeclaredFields()) {
                    if (f.getType() == boolean.class) {
                        f.setAccessible(true);
                        f.setBoolean(settings, JByteCustom.ops.get("procyon" + f.getName()).getBoolean());
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            settings.setShowSyntheticMembers(true);
            MetadataSystem metadataSystem = new MetadataSystem(new ITypeLoader() {
                private InputTypeLoader backLoader = new InputTypeLoader();

                @Override
                public boolean tryLoadType(String s, Buffer buffer) {
                    if (s.equals(cn.name)) {
                        buffer.putByteArray(b, 0, b.length);
                        buffer.position(0);
                        return true;
                    } else {
                        return backLoader.tryLoadType(s, buffer);
                    }
                }
            });
            TypeReference type = metadataSystem.lookupType(cn.name);
            DecompilationOptions decompilationOptions = new DecompilationOptions();
            decompilationOptions.setSettings(DecompilerSettings.javaDefaults());
            decompilationOptions.setFullDecompilation(true);
            TypeDefinition resolvedType = null;
            if (type == null || ((resolvedType = type.resolve()) == null)) {
                new ErrorDisplay("Unable to resolve type.");
                return "error";
            }
            StringWriter stringwriter = new StringWriter();
            settings.getLanguage().decompileType(resolvedType, new PlainTextOutput(stringwriter), decompilationOptions);
            String decompiledSource = stringwriter.toString();
            // :trollface:
            DecompilerOutput.decompiledClass = decompiledSource;
            return decompiledSource;
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
    }
}
