package me.synnk.jbytecustom.decompiler;

import codes.som.anthony.koffee.disassembler.ClassDisassemblyKt;
import codes.som.anthony.koffee.disassembler.MethodDisassemblyKt;
import codes.som.anthony.koffee.disassembler.util.DisassemblyContext;
import codes.som.anthony.koffee.disassembler.util.SourceCodeGenerator;
import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.DecompilerPanel;
import org.objectweb.asm.tree.MethodNode;

public class KoffeeDecompiler extends Decompiler {
    public KoffeeDecompiler(JByteCustom jbm, DecompilerPanel dp) {
        super(jbm, dp);
    }

    @Override
    public String decompile(byte[] b, MethodNode mn) {
        SourceCodeGenerator sourceCodeGenerator = new SourceCodeGenerator();
        DisassemblyContext context = new DisassemblyContext(cn.name);
        if (mn == null) {
            return ClassDisassemblyKt.disassemble(cn);
        } else {
            MethodDisassemblyKt.disassembleMethod(mn, sourceCodeGenerator, context);
        }
        // :trollface:

        return sourceCodeGenerator.toString();
    }
}