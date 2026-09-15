$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
$im=[Drawing.Bitmap]::new((Join-Path $root 'native/03-color-draft.png'))
# Replay the exact recorded Piskel pencil actions after browser downloads failed.
$edits=@()
foreach($p in @(@(12,1),@(8,2),@(6,3),@(4,4),@(3,6),@(4,8),@(5,10),@(6,11),@(5,12),@(6,12))){$edits+=@{x=$p[0];y=$p[1];color='#FFF2BC'}}
$edits+=@{x=13;y=1;color='#FFCF54'}
foreach($p in @(@(1,12),@(11,12))){$edits+=@{x=$p[0];y=$p[1];color='#EB8B32'}}
foreach($e in $edits){$im.SetPixel($e.x,$e.y,[Drawing.ColorTranslator]::FromHtml($e.color))}
$final=Join-Path $root 'native/lunge-curve-v1.png'
$im.Save($final,[Drawing.Imaging.ImageFormat]::Png)
$pixels=@();for($y=0;$y -lt 16;$y++){$row=@();for($x=0;$x -lt 16;$x++){$p=$im.GetPixel($x,$y);$row+=('{0:X2}{1:X2}{2:X2}{3:X2}' -f $p.R,$p.G,$p.B,$p.A)};$pixels+=,@($row)}
@{width=16;height=16;format='RGBA hex rows';pixels=$pixels;editorEdits=$edits;provenance='Native-grid draft plus replayed exact Piskel pencil actions; browser PNG download did not complete.'} | ConvertTo-Json -Depth 8 | Set-Content -Encoding utf8 (Join-Path $root 'native/lunge-curve-v1.source.json')
$board=[Drawing.Bitmap]::new(820,490);$g=[Drawing.Graphics]::FromImage($board);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode=[Drawing.Drawing2D.InterpolationMode]::NearestNeighbor;$g.PixelOffsetMode=[Drawing.Drawing2D.PixelOffsetMode]::Half
$font=[Drawing.Font]::new('Segoe UI',13);$small=[Drawing.Font]::new('Segoe UI',10);$white=[Drawing.Brushes]::White
$g.DrawString('LUNGE / 16 x 16 PIXEL-ART CANDIDATE', $font,$white,24,18)
$g.DrawString('Actual PNG at 1x, 3x and 16x. Enlargements use nearest-neighbor.', $small,$white,24,49)
$g.DrawImage($im,[Drawing.Rectangle]::new(26,105,16,16),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
$g.DrawImage($im,[Drawing.Rectangle]::new(80,88,48,48),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
$g.DrawString('Refined candidate', $small,$white,24,155)
$g.DrawImage($im,[Drawing.Rectangle]::new(24,187,256,256),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
$before=[Drawing.Bitmap]::new((Join-Path $root 'native/03-color-draft.png'))
$g.DrawString('Before editor refinement', $small,$white,304,155)
$g.DrawImage($before,[Drawing.Rectangle]::new(304,187,256,256),0,0,16,16,[Drawing.GraphicsUnit]::Pixel);$before.Dispose()
$g.FillRectangle([Drawing.Brushes]::LightGray,595,86,192,192)
$g.DrawImage($im,[Drawing.Rectangle]::new(595,86,192,192),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
$g.DrawString('Light background / 12x', $small,$white,590,293)
$g.DrawString('Concept preserved:', $small,$white,590,341)
$g.DrawString('continuous curve', $small,$white,590,365)
$g.DrawString('launch ripple', $small,$white,590,389)
$g.DrawString('ivory / gold / orange', $small,$white,590,413)
$g.Dispose();$board.Save((Join-Path $root 'lunge-curve-v1-review.png'),[Drawing.Imaging.ImageFormat]::Png);$board.Dispose();$im.Dispose()
